@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package andy.zhu.minesweeper.screen

import MousePointerButton
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.GameInstance
import andy.zhu.minesweeper.MineCanvasColor
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.extensions.inverted
import andy.zhu.minesweeper.extensions.scale
import andy.zhu.minesweeper.extensions.scaleX
import andy.zhu.minesweeper.extensions.scaleY
import andy.zhu.minesweeper.extensions.transformed
import andy.zhu.minesweeper.navigation.MainGameScreenComponent
import mousePointerMatcher
import onPointerEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGameScreen(component: MainGameScreenComponent) {
    val gameInstance by component.gameInstance.collectAsState()
    val mapUI by gameInstance.mapUIFlow.collectAsState()
    val textMeasure = rememberTextMeasurer()
    var transform by remember { mutableStateOf<CanvasTransform>(CanvasTransform.InitialTransform(IntSize.Zero)) }
    val canvasMatrix = remember { Animatable(Matrix(), MatrixConverter) }

    val mineDrawConfig = CreateDrawConfig()
    val density = LocalDensity.current

    val canvasPaddings = with(LocalDensity.current) {
        val defaultPadding = MineDrawConfig.canvasPadding.toPx()
        listOf(
            defaultPadding,
            WindowInsets.statusBars.getTop(this) + 66.dp.toPx() + defaultPadding,
            defaultPadding,
            WindowInsets.navigationBars.getBottom(this) + defaultPadding
        )
    }

    fun CanvasTransform.matrix(): Matrix = when(this) {
        is CanvasTransform.InitialTransform -> {
            val contentInner = Rect(
                canvasPaddings[0], canvasPaddings[1],
                canvasSize.width - canvasPaddings[2],
                canvasSize.height - canvasPaddings[3]
            )
            val mineSize = with(density) {
                Size(mineDrawConfig.mineSize.toPx(), mineDrawConfig.mineSize.toPx())
            }
            calcInitMatrix(mineSize, component.level.width, component.level.height, contentInner)
        }
        is CanvasTransform.FixedTransform -> matrix
        is CanvasTransform.AnimatedTransform -> matrix
    }

    LaunchedEffect(transform) {
        when(transform) {
            is CanvasTransform.InitialTransform -> {
                canvasMatrix.snapTo(transform.matrix())
            }
            is CanvasTransform.FixedTransform -> {
                canvasMatrix.snapTo(transform.matrix())
            }
            is CanvasTransform.AnimatedTransform -> {
                canvasMatrix.animateTo(transform.matrix(), animationSpec = tween(200))
            }
        }
    }

    fun calcMinePosition(pointerOffset: Offset, mustInsideBorder: Boolean = true): IntOffset? {
        val mineSize = with(density) { mineDrawConfig.mineSize.toPx() }
        val (x, y) = canvasMatrix.value.inverted().map(pointerOffset)
        val xPos = (x / mineSize).toInt()
        val yPos = (y / mineSize).toInt()
        return if (!mustInsideBorder) {
            IntOffset(xPos, yPos)
        } else {
            val minePadding = with(density) { mineDrawConfig.padding.toPx() }
            val insideBorderRect = Rect(
                Offset(xPos * mineSize + minePadding, yPos * mineSize + minePadding),
                Size(mineSize - minePadding * 2, mineSize - minePadding * 2)
            )
            if (insideBorderRect.contains(canvasMatrix.value.inverted().map(pointerOffset))) {
                IntOffset(xPos, yPos)
            } else {
                null
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                ),
                title = { Title(gameInstance) },
                navigationIcon = {
                    IconButton(onClick = component.onClose) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = component::onRefresh) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        floatingActionButton = { MineFab(gameInstance) }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            calcMinePosition(it)?.let {
                                gameInstance.onMineLongTap(gameInstance.Position(it.x, it.y))
                            }
                        },
                        onTap = {
                            calcMinePosition(it)?.let {
                                gameInstance.onMineTap(gameInstance.Position(it.x, it.y))
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        val currentMatrix = transform.matrix()
                        val scale = limitScale(currentMatrix, zoom)
                        val targetMatrix = currentMatrix.transformed {
                            translate(pan.x / scaleX(), pan.y / scaleY())
                            if (scale != 1f) {
                                scale(scale, scale, centroid.x, centroid.y)
                            }
                        }
                        transform = CanvasTransform.FixedTransform(targetMatrix)
                    }
                }
                .mousePointerMatcher(MousePointerButton.Secondary) {
                    calcMinePosition(it)?.let {
                        gameInstance.onMineRightClick(gameInstance.Position(it.x, it.y))
                    }
                }
                .mousePointerMatcher(MousePointerButton.Tertiary) {
                    calcMinePosition(it, mustInsideBorder = false)?.let {
                        gameInstance.onMineMiddleClick(gameInstance.Position(it.x, it.y))
                    }
                }
                .onPointerEvent(PointerEventType.Scroll) {
                    val pointerInputChange = it.changes.firstOrNull() ?: return@onPointerEvent
                    val currentMatrix = transform.matrix()
                    val position = pointerInputChange.position
                    val scrollOffset = pointerInputChange.scrollDelta.y
                    val scale = limitScale(currentMatrix, if (scrollOffset > 0) 0.8f else 1.25f)
                    if (scale != 1f) {
                        val targetMatrix = currentMatrix.transformed {
                            scale(scale, scale, position.x, position.y)
                        }
                        transform = CanvasTransform.AnimatedTransform(targetMatrix)
                    }
                }
                .onPointerEvent(PointerEventType.Move) {
                    val offset = it.changes.firstOrNull()?.position ?: return@onPointerEvent
                    val minePosition = calcMinePosition(offset)
                    if (minePosition != null) {
                        gameInstance.onMineHover(gameInstance.Position(minePosition.x, minePosition.y))
                    } else {
                        gameInstance.onMineHover(null)
                    }
                }
                .onPointerEvent(PointerEventType.Exit) {
                    gameInstance.onMineHover(null)
                }
                .onGloballyPositioned { coordinates ->
                    val initialTransform = (transform as? CanvasTransform.InitialTransform) ?: return@onGloballyPositioned
                    if (coordinates.size != initialTransform.canvasSize) {
                        transform = CanvasTransform.InitialTransform(coordinates.size)
                    }
                }
        ) {
            drawMines(canvasMatrix.value, mapUI, mineDrawConfig)
        }
    }


    val succeed by gameInstance.gameWin.collectAsState(false)
    val showSuccessDialog = remember { mutableStateOf(false) }
    LaunchedEffect(succeed) {
        showSuccessDialog.value = succeed
    }
    if (showSuccessDialog.value) {
        SuccessDialog { showSuccessDialog.value = false }
    }

    val failed by gameInstance.gameOver.collectAsState(false)
    val showFailedDialog = remember { mutableStateOf(false) }
    LaunchedEffect(failed) {
        showFailedDialog.value = failed
    }
    if (showFailedDialog.value) {
        FailureDialog { showFailedDialog.value = false }
    }
}

@Composable
private fun SuccessDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Ok")
            }
        },
        onDismissRequest = onDismissRequest,
        text = {
            Text("Congratuation!")
        }
    )
}

@Composable
private fun Title(gameInstance: GameInstance) {
    val timeString by gameInstance.timeString.collectAsState("0")
    val minesRemaining by gameInstance.minesRemainingText.collectAsState("")

    Row {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource("clock.xml"),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                timeString,
                modifier = Modifier.padding(start = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 24.dp),
        ) {
            Icon(
                painter = painterResource("mine.xml"),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                minesRemaining,
                modifier = Modifier.padding(start = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun MineFab(gameInstance: GameInstance) {
    val flagWhenTap by gameInstance.flagWhenTap.collectAsState()

    if (gameInstance.showFab) {
        FloatingActionButton(
            onClick = gameInstance::switchTapAction,
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                painter = painterResource(if (flagWhenTap) "flag.xml" else "mine.xml"),
                contentDescription = "",
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@Composable
private fun FailureDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Ok")
            }
        },
        onDismissRequest = onDismissRequest,
        text = {
            Text("😢")
        }
    )
}

@Composable
private fun CreateDrawConfig(): MineDrawConfig {
    return MineDrawConfig.Image(
        MineCanvasColor.fromColorScheme(MaterialTheme.colorScheme),
        (0 until 10).map { painterResource("numeric_$it.png") },
        painterResource("mine.png"),
        painterResource("flag.png"),
        painterResource("question_mark.png"),
    )
}

private fun calcInitMatrix(mineSize: Size, width: Int, height: Int, contentInner: Rect): Matrix {
    val minesSize = Size(mineSize.width * width, mineSize.height * height)

    val targetRect = if (minesSize.width < contentInner.width && minesSize.height < contentInner.height) {
        Rect(Offset((contentInner.width - minesSize.width) / 2 + contentInner.left,
            (contentInner.height - minesSize.height) / 2 + contentInner.top), minesSize)
    } else {
        val minesRatio = minesSize.width / minesSize.height
        val contentRatio = contentInner.width / contentInner.height
        if (minesRatio < contentRatio) {
            val targetHeight = contentInner.height
            val targetWidth = minesRatio * targetHeight
            val offsetX = (contentInner.width - targetWidth) / 2 + contentInner.left
            val offsetY = contentInner.top
            Rect(Offset(offsetX, offsetY), Size(targetWidth, targetHeight))
        } else {
            val targetWidth = contentInner.width
            val targetHeight = targetWidth / minesRatio
            val offsetX = contentInner.left
            val offsetY = (contentInner.height - targetHeight) / 2 + contentInner.top
            Rect(Offset(offsetX, offsetY), Size(targetWidth, targetHeight))
        }
    }

    val translateX = targetRect.left
    val translateY = targetRect.top
    val scaleX = targetRect.width / minesSize.width
    val scaleY = targetRect.height / minesSize.height
    return Matrix().apply {
        translate(translateX, translateY)
        scale(scaleX, scaleY)
    }
}

object MatrixConverter : TwoWayConverter<Matrix, AnimationVector4D> {
    override val convertFromVector: (AnimationVector4D) -> Matrix = { vector ->
        Matrix().apply {
            values[Matrix.TranslateX] = vector.v1
            values[Matrix.TranslateY] = vector.v2
            values[Matrix.ScaleX] = vector.v3
            values[Matrix.ScaleY] = vector.v4
        }
    }

    override val convertToVector: (Matrix) -> AnimationVector4D = { matrix ->
        AnimationVector4D(
            matrix.values[Matrix.TranslateX], matrix.values[Matrix.TranslateY],
            matrix.values[Matrix.ScaleX], matrix.values[Matrix.ScaleY]
        )
    }
}

sealed class CanvasTransform {
    data class InitialTransform(val canvasSize: IntSize) : CanvasTransform()
    data class FixedTransform(val matrix: Matrix) : CanvasTransform()
    data class AnimatedTransform(val matrix: Matrix) : CanvasTransform()
}

private const val MIN_SCALE = 0.3f
private const val MAX_SCALE = 5f

private fun limitScale(currentMatrix: Matrix, scale: Float): Float {
    return if (scale < 1) {
        if (currentMatrix.scaleX() <= MIN_SCALE) {
            1f
        } else if (currentMatrix.scaleX() * scale < MIN_SCALE) {
            MIN_SCALE / currentMatrix.scaleX()
        } else {
            scale
        }
    } else {
        if (currentMatrix.scaleX() >= MAX_SCALE) {
            1f
        } else if (currentMatrix.scaleX() * scale > MAX_SCALE) {
            MAX_SCALE / currentMatrix.scaleX()
        } else {
            scale
        }
    }
}
