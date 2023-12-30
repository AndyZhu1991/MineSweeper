package andy.zhu.minesweeper.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.navigation.MainGameScreenComponent
import mousePointerMatcher
import onPointerEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainGameScreen(component: MainGameScreenComponent) {
    val mapUI by component.gameInstance.mapUIFlow.collectAsState()
    val textMeasure = rememberTextMeasurer()
    var matrix by remember { mutableStateOf(Matrix()) }
    var animateTargetMatrix by remember { mutableStateOf(Matrix()) }
    val animatableMatrix = remember {
        Animatable(Matrix(), MatrixConverter)
    }

    LaunchedEffect(matrix, animateTargetMatrix) {
        if (matrix == animateTargetMatrix) {
            animatableMatrix.animateTo(animateTargetMatrix, animationSpec = tween(200))
        } else {
            animatableMatrix.snapTo(matrix)
        }
    }

    val mineSizePx = with(LocalDensity.current) {
        MineDrawConfig.mineSize.toSize()
    }

    fun calcMinePosition(pointerOffset: Offset): IntOffset {
        val (x, y) = animatableMatrix.value.inverted().map(pointerOffset)
        val xPos = (x / mineSizePx.width).toInt()
        val yPos = (y / mineSizePx.height).toInt()
        return IntOffset(xPos, yPos)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        val (x, y) = calcMinePosition(it)
                        component.gameInstance.onMineRightClick(y, x)
                    },
                    onTap = {
                        val (x, y) = calcMinePosition(it)
                        component.gameInstance.onMineTap(y, x)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    matrix = matrix.transformed {
                        translate(dragAmount.x / scaleX(), dragAmount.y / scaleY())
                    }
                }
            }
            .mousePointerMatcher(MousePointerButton.Secondary) {
                val (x, y) = calcMinePosition(it)
                component.gameInstance.onMineRightClick(y, x)
            }
            .mousePointerMatcher(MousePointerButton.Tertiary) {
                val (x, y) = calcMinePosition(it)
                component.gameInstance.onMineMiddleClick(y, x)
            }
            .onPointerEvent(PointerEventType.Scroll) {
                val pointerInputChange = it.changes.firstOrNull() ?: return@onPointerEvent
                val position = pointerInputChange.position
                val scrollOffset = pointerInputChange.scrollDelta.y
                val scale = if (scrollOffset > 0) 0.8f else 1.25f
                matrix = matrix.transformed {
                    scale(scale, scale, position.x, position.y)
                }
                animateTargetMatrix = matrix
            }
    ) {
        withTransform(
            transformBlock = { transform(animatableMatrix.value) }
        ) {
            drawMines(mapUI, textMeasure, MineDrawConfig)
        }
    }
}

object MineDrawConfig {
    val mineSize = DpSize(48.dp, 48.dp)
    val padding = 2.dp
    val corner = 4.dp
    val mineInnerSize = DpSize(mineSize.width - padding, mineSize.height - padding)
    val itemTextStyle = TextStyle(fontSize = 32.sp)
    val hiddenItemColor = Color.Gray.copy(alpha = 0.66f)
}

private fun Matrix.transformed(block: Matrix.() -> Unit): Matrix {
    return Matrix(values.copyOf()).apply(block)
}

private fun Matrix.copy(): Matrix {
    return Matrix(values.copyOf())
}

private fun Matrix.inverted(): Matrix {
    return transformed { invert() }
}

private fun Matrix.scaleX(): Float {
    return this.values[Matrix.ScaleX]
}

private fun Matrix.scaleY(): Float {
    return this.values[Matrix.ScaleY]
}

private fun Matrix.translateX(): Float {
    return this.values[Matrix.TranslateX]
}

private fun Matrix.translateY(): Float {
    return this.values[Matrix.TranslateY]
}

private fun Matrix.scale(scaleX: Float, scaleY: Float, pivotX: Float, pivotY: Float) {
    val deltaX = (pivotX - translateX()) / this.scaleX()
    val deltaY = (pivotY - translateY()) / this.scaleY()
    translate(deltaX, deltaY)
    scale(scaleX, scaleY)
    translate(-deltaX, -deltaY)
}

private fun Matrix.postTranslate(translateX: Float, translateY: Float) {
    translate(translateX / scaleX(), translateY / scaleY())
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
