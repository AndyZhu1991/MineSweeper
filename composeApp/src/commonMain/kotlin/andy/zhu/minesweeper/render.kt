package andy.zhu.minesweeper

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.extensions.inverted
import andy.zhu.minesweeper.screen.MineDrawConfig
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


internal fun DrawScope.drawMines(
    transform: Matrix,
    map: GameInstance.MineMapUI,
    textMeasurer: TextMeasurer,
    drawConfig: MineDrawConfig,
    mineCanvasPainters: MineCanvasPainters,
) {
    val invertedMatrix = transform.inverted()
    val canvasRect = Rect(Offset.Zero, size)
    val viewPort = invertedMatrix.map(canvasRect)
    val mineWidth = drawConfig.mineSize.width.toPx()
    val mineHeight = drawConfig.mineSize.height.toPx()
    val mineLeft = (viewPort.left / mineWidth).toInt()
    val mineTop = (viewPort.top / mineHeight).toInt()
    val mineRight = (viewPort.right / mineWidth).toInt()
    val mineBottom = (viewPort.bottom / mineHeight).toInt()
    map.enumeratedItems
        .filter { (y, x, item) ->
            x in mineLeft..mineRight && y in mineTop..mineBottom
        }
        .forEach { (y, x, item) ->
            drawMine(transform, item, textMeasurer, drawConfig,Offset(x * mineWidth, y * mineHeight), mineCanvasPainters)
        }
}

private fun DrawScope.drawMine(
    transform: Matrix,
    item: GameInstance.MineItemUI,
    textMeasurer: TextMeasurer,
    drawConfig: MineDrawConfig,
    offset: Offset,
    mineCanvasPainters: MineCanvasPainters,
) {
    withTransform(
        transformBlock = { transform(transform) }
    ) {
        val paddingOffset = offset + Offset(drawConfig.minePadding.toPx(), drawConfig.minePadding.toPx())
        val outerSize = drawConfig.mineSize.toSize()
        val innerSize = drawConfig.borderSize.toSize()
        if (item != GameInstance.MineItemUI.Hidden) {
            drawRoundRect(
                Color.Gray, paddingOffset, innerSize,
                CornerRadius(drawConfig.mineCorner.toPx()), Stroke(1.dp.toPx())
            )
        }
        when (item) {
            GameInstance.MineItemUI.Hidden -> {
                drawRoundRect(drawConfig.hiddenItemColor, paddingOffset, innerSize, CornerRadius(drawConfig.mineCorner.toPx()))
            }

            GameInstance.MineItemUI.Flagged -> {
                drawTextAtCenter(textMeasurer, "🚩", outerSize, drawConfig.itemTextStyle, offset)
            }

            GameInstance.MineItemUI.Uncertain -> {
                drawTextAtCenter(textMeasurer, "❓", outerSize, drawConfig.itemTextStyle, offset)
            }

            GameInstance.MineItemUI.OpenedBoom -> {
                drawTextAtCenter(textMeasurer, "💥", outerSize, drawConfig.itemTextStyle, offset)
            }

            GameInstance.MineItemUI.MineView -> {
                drawTextAtCenter(textMeasurer, "💣", outerSize, drawConfig.itemTextStyle, offset)
            }

            is GameInstance.MineItemUI.Opened -> {
                if (item.num != 0) {
                    drawTextAtCenter(textMeasurer, item.num.toString(), outerSize, drawConfig.itemTextStyle, offset)
                }
            }
        }
    }
}

private fun DrawScope.drawTextAtCenter(
    textMeasurer: TextMeasurer,
    text: String,
    size: Size,
    style: TextStyle,
    topLeft: Offset = Offset.Zero,
) {
    val textLayoutResult = textMeasurer.measure(text, style)
    val drawRect = Rect(topLeft, size)
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        style = style,
        topLeft = Offset(
            x = drawRect.center.x - textLayoutResult.size.width / 2,
            y = drawRect.center.y - textLayoutResult.size.height / 2,
        ),
        size = Size(textLayoutResult.size.width.toFloat(), textLayoutResult.size.height.toFloat()),
    )
}

private fun DrawScope.drawMineWithVector(
    transform: Matrix,
    item: GameInstance.MineItemUI,
    textMeasurer: TextMeasurer,
    drawConfig: MineDrawConfig,
    offset: Offset,
    mineCanvasPainters: MineCanvasPainters,
) {
    val paddingOffset = offset + Offset(drawConfig.minePadding.toPx(), drawConfig.minePadding.toPx())

    withTransform(
        transformBlock = { transform(transform) }
    ) {
        val innerSize = drawConfig.borderSize.toSize()
        if (item != GameInstance.MineItemUI.Hidden) {
            drawRoundRect(
                Color.Gray, paddingOffset, innerSize,
                CornerRadius(drawConfig.mineCorner.toPx()), Stroke(1.dp.toPx())
            )
        } else {
            drawRoundRect(drawConfig.hiddenItemColor, paddingOffset, innerSize, CornerRadius(drawConfig.mineCorner.toPx()))
        }
    }

    val vectorOffset = paddingOffset + Offset(drawConfig.innerPadding.toPx(), drawConfig.innerPadding.toPx())
    val vectorRect = Rect(vectorOffset, drawConfig.vectorSize.toSize())
    val mappedRect = transform.map(vectorRect)
    when (item) {
        GameInstance.MineItemUI.Hidden -> Unit

        GameInstance.MineItemUI.Flagged -> drawVector(mineCanvasPainters.flag, mappedRect)

        GameInstance.MineItemUI.Uncertain -> drawVector(mineCanvasPainters.questionMark, mappedRect)

        GameInstance.MineItemUI.OpenedBoom -> drawVector(mineCanvasPainters.mine, mappedRect)

        GameInstance.MineItemUI.MineView -> drawVector(mineCanvasPainters.mine, mappedRect)

        is GameInstance.MineItemUI.Opened -> {
            if (item.num != 0) {
                drawVector(mineCanvasPainters.numbers[item.num], mappedRect)
            }
        }
    }
}

private fun DrawScope.drawVector(painter: Painter, rect: Rect) {
    translate(rect.left, rect.top) {
        with(painter) {
            draw(rect.size)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadMineCanvasPainter() = MineCanvasPainters(
    (0 until 10).map { painterResource("numeric_$it.xml") },
    painterResource("mine.xml"),
    painterResource("flag.xml"),
    painterResource("question_mark.xml"),
)

class MineCanvasPainters(
    val numbers: List<Painter>,
    val mine: Painter,
    val flag: Painter,
    val questionMark: Painter,
)