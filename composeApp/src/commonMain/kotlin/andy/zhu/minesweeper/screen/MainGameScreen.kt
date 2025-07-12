/**
 * Main UI implementation for the Minesweeper game.
 *
 * This file contains the main UI components for Minesweeper, including:
 * - Game board rendering and interaction handling
 * - Top status bar (showing time and remaining mines)
 * - Game control buttons (back, refresh)
 * - Game state dialogs (win, fail)
 *
 * The UI supports the following interactions:
 * - Tap: reveal a cell
 * - Long press: flag a mine
 * - Pinch/zoom gesture: scale the view
 * - Mouse right click: flag a mine
 * - Mouse middle click: quick open surrounding cells
 */
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package andy.zhu.minesweeper.screen

import MousePointerButton
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import andy.zhu.minesweeper.MineCanvasColor
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.game.GameInstance
import andy.zhu.minesweeper.inverted
import andy.zhu.minesweeper.navigation.MainGameScreenComponent
import andy.zhu.minesweeper.scale
import andy.zhu.minesweeper.scaleX
import andy.zhu.minesweeper.scaleY
import andy.zhu.minesweeper.transformed
import minesweeper.composeapp.generated.resources.Res
import minesweeper.composeapp.generated.resources.*
import mousePointerMatcher
import onPointerEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/**
 * Main screen composable for the Minesweeper game.
 *
 * @param component The main game screen component, containing game state and control logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGameScreen(component: MainGameScreenComponent) {
    // Game instance state
    val gameInstance by component.gameInstance.collectAsState()
    val mapUI by gameInstance.mapUIFlow.collectAsState()
    val textMeasure = rememberTextMeasurer()
    
    // Canvas transform state
    var transform by remember { mutableStateOf<CanvasTransform>(CanvasTransform.InitialTransform(IntSize.Zero)) }
    val canvasMatrix = remember { Animatable(Matrix(), MatrixConverter) }
    val canvasAnimationFraction = remember { Animatable(1f, Float.VectorConverter) }

    // Handle map UI animation
    LaunchedEffect(mapUI) {
        if (mapUI.hasAnimation) {
            canvasAnimationFraction.snapTo(0f)
            canvasAnimationFraction.animateTo(1f, tween(mapUI.animationDuration))
            gameInstance.onAnimationFinished()
        } else {
            if (canvasAnimationFraction.isRunning) {
                canvasAnimationFraction.stop()
            }
            canvasAnimationFraction.snapTo(0f)
        }
    }

    // Create draw config and get screen density
    val mineDrawConfig = createDrawConfig()
    val density = LocalDensity.current

    // Calculate canvas paddings, considering status and navigation bars
    val canvasPaddings = with(LocalDensity.current) {
        val defaultPadding = MineDrawConfig.canvasPadding.toPx()
        listOf(
            defaultPadding,
            WindowInsets.statusBars.getTop(this) + 66.dp.toPx() + defaultPadding,
            defaultPadding,
            WindowInsets.navigationBars.getBottom(this) + defaultPadding
        )
    }

    /**
     * Calculate the canvas transformation matrix.
     * Returns the corresponding matrix based on the current transform state.
     */
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
            calcInitMatrix(mineSize, component.gameConfig.width, component.gameConfig.height, contentInner)
        }
        is CanvasTransform.FixedTransform -> matrix
        is CanvasTransform.AnimatedTransform -> matrix
    }

    // Listen to canvas transform state and perform corresponding animation
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

    /**
     * Calculate the mine grid position corresponding to a pointer offset.
     *
     * @param pointerOffset The pointer offset on the canvas
     * @param mustInsideBorder Whether the pointer must be inside the cell border
     * @return The mine grid coordinate, or null if not valid
     */
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = { Title(gameInstance) },
                navigationIcon = {
                    IconButton(onClick = component.onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = component::onRefresh) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
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
            drawMines(
                canvasMatrix.value,
                mapUI,
                canvasAnimationFraction.value,
                mineDrawConfig
            )
        }
    }


    val winInfo by gameInstance.gameWinInfo.collectAsState(null)
    val showSuccessDialog = remember { mutableStateOf(false) }
    LaunchedEffect(winInfo) {
        showSuccessDialog.value = winInfo != null
    }
    if (showSuccessDialog.value && winInfo != null) {
        SuccessDialog(winInfo!!) { showSuccessDialog.value = false }
    }

    val failed by gameInstance.gameOver.collectAsState(false)
    val showFailedDialog = remember { mutableStateOf(false) }
    LaunchedEffect(failed) {
        showFailedDialog.value = failed
    }
    if (showFailedDialog.value) {
        FailureDialog(
            onDismissRequest = { showFailedDialog.value = false },
            onRetry = component::onRefresh,
        )
    }
}

/**
 * Game win dialog.
 *
 * @param gameWinInfo Game win info, including ranking data
 * @param onDismissRequest Callback when dialog is dismissed
 */
@Composable
private fun SuccessDialog(
    gameWinInfo: GameInstance.GameWinInfo,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Ok")
            }
        },
        onDismissRequest = onDismissRequest,
        title = {
            Text("Congratulations!")
        },
        text = {
            Column(
                modifier = Modifier.width(280.dp),
            ) {
                gameWinInfo.records.forEachIndexed { index, recordItem ->
                    RankLine(index + 1, (recordItem.time_millis / 1000).toInt(),
                        recordItem.created_at ,gameWinInfo.yourRank == index)
                }
            }
        }
    )
}

/**
 * Top status bar title composable.
 * Shows game time and remaining mines.
 *
 * @param gameInstance Game instance providing time and mine info
 */
@Composable
private fun Title(gameInstance: GameInstance) {
    val timeString by gameInstance.timeString.collectAsState("0")
    val minesRemaining by gameInstance.minesRemainingText.collectAsState("")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.clock),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            Text(
                timeString,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Row(
            modifier = Modifier.offset(y = (-2).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.mine),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
            Text(
                minesRemaining,
                modifier = Modifier.padding(start = 2.dp, bottom = 1.dp),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

/**
 * Floating action button for the game.
 * Used to switch tap action (reveal/flag).
 *
 * @param gameInstance Game instance controlling button state and behavior
 */
@Composable
private fun MineFab(gameInstance: GameInstance) {
    val flagWhenTap by gameInstance.flagWhenTap.collectAsState()

    if (gameInstance.showFab) {
        FloatingActionButton(
            onClick = gameInstance::switchTapAction,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Icon(
                painter = painterResource(if (flagWhenTap) Res.drawable.flag else Res.drawable.mine),
                contentDescription = "",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

/**
 * Game fail dialog.
 *
 * @param onDismissRequest Callback when dialog is dismissed
 * @param onRetry Callback to retry the game
 */
@Composable
private fun FailureDialog(
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
) {
    AlertDialog(
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onRetry()
                }
            ) {
                Text("Try Again!")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
        onDismissRequest = onDismissRequest,
        title = {
            Text("Game Over")
        },
        text = {
            Text("You failed!😢")
        },
        tonalElevation = 10.dp,
    )
}

/**
 * Create mine draw config.
 * Config includes number, mine, flag and other image resources.
 */
@Composable
fun createDrawConfig(): MineDrawConfig {
    return MineDrawConfig.Image(
        MineCanvasColor.fromColorScheme(MaterialTheme.colorScheme),
        listOf(
            painterResource(Res.drawable.numeric_0_png),
            painterResource(Res.drawable.numeric_1_png),
            painterResource(Res.drawable.numeric_2_png),
            painterResource(Res.drawable.numeric_3_png),
            painterResource(Res.drawable.numeric_4_png),
            painterResource(Res.drawable.numeric_5_png),
            painterResource(Res.drawable.numeric_6_png),
            painterResource(Res.drawable.numeric_7_png),
            painterResource(Res.drawable.numeric_8_png),
            painterResource(Res.drawable.numeric_9_png),
        ),
        painterResource(Res.drawable.mine_png),
        painterResource(Res.drawable.flag_png),
        painterResource(Res.drawable.question_mark_png),
    )
}

/**
 * Calculate the initial transformation matrix.
 * Computes appropriate scaling and translation based on mine size and game area.
 *
 * @param mineSize Size of a single mine cell
 * @param width Game area width (number of cells)
 * @param height Game area height (number of cells)
 * @param contentInner Rect of the content area
 * @return Initial transformation matrix
 */
private fun calcInitMatrix(mineSize: Size, width: Int, height: Int, contentInner: Rect): Matrix {
    val minesSize = Size(mineSize.width * width, mineSize.height * height)

    val targetSize = if (minesSize.width < contentInner.width && minesSize.height < contentInner.height) {
        minesSize
    } else {
        val minesRatio = minesSize.width / minesSize.height
        val contentRatio = contentInner.width / contentInner.height
        if (minesRatio < contentRatio) {
            val targetHeight = contentInner.height
            val targetWidth = minesRatio * targetHeight
            Size(targetWidth, targetHeight)
        } else {
            val targetWidth = contentInner.width
            val targetHeight = targetWidth / minesRatio
            Size(targetWidth, targetHeight)
        }
    }.let {
        if (it.width / minesSize.width < MIN_SCALE) {
            Size(minesSize.width * MIN_SCALE, minesSize.height * MIN_SCALE)
        } else {
            it
        }
    }

    val offset = Offset((contentInner.width - targetSize.width) / 2 + contentInner.left,
                     (contentInner.height - targetSize.height) / 2 + contentInner.top)
    val targetRect = Rect(offset, targetSize)

    val translateX = targetRect.left
    val translateY = targetRect.top
    val scaleX = targetRect.width / minesSize.width
    val scaleY = targetRect.height / minesSize.height
    return Matrix().apply {
        translate(translateX, translateY)
        scale(scaleX, scaleY)
    }
}

/**
 * Matrix animation converter.
 * Used for matrix animation interpolation.
 */
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

/**
 * Sealed class for canvas transform state.
 */
sealed class CanvasTransform {
    /** Initial transform state, contains canvas size */
    data class InitialTransform(val canvasSize: IntSize) : CanvasTransform()
    /** Fixed transform state, contains matrix */
    data class FixedTransform(val matrix: Matrix) : CanvasTransform()
    /** Animated transform state, contains target matrix */
    data class AnimatedTransform(val matrix: Matrix) : CanvasTransform()
}

// Scale limit constants
private const val MIN_SCALE = 0.3f
private const val MAX_SCALE = 5f

/**
 * Limit the scale factor.
 * Ensures scaling stays within allowed range.
 *
 * @param currentMatrix Current transformation matrix
 * @param scale Target scale factor
 * @return Adjusted scale factor
 */
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
