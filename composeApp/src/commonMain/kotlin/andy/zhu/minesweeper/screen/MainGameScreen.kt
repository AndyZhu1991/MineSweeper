package andy.zhu.minesweeper.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
    val matrix = remember { Matrix() }
    var invalidations by remember { mutableStateOf(0) }

    val mineSizePx = with(LocalDensity.current) {
        MineDrawConfig.mineSize.toSize()
    }

    fun calcMinePosition(pointerOffset: Offset): IntOffset {
        val (x, y) = matrix.inverted().map(pointerOffset)
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
                detectDragGestures { change, dragAmount ->
                    matrix.translate(dragAmount.x / matrix.scaleX(), dragAmount.y / matrix.scaleY())
                    invalidations++
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
                matrix.scale(scale, scale, position.x, position.y)
                invalidations++
            }
    ) {
        invalidations.let {
            withTransform(
                transformBlock = { transform(matrix) }
            ) {
                drawMines(mapUI, textMeasure, MineDrawConfig)
            }
        }
    }
}

object MineDrawConfig {
    val mineSize = DpSize(48.dp, 48.dp)
    val padding = 2.dp
    val corner = 4.dp
    val mineInnerSize = DpSize(mineSize.width - padding, mineSize.height - padding)
    val itemTextStyle = TextStyle(fontSize = 32.sp)
}

private fun Matrix.inverted(): Matrix {
    return Matrix(values.copyOf()).apply { invert() }
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
