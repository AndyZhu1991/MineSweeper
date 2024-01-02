package andy.zhu.minesweeper

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.screen.MineDrawConfig


internal fun DrawScope.drawMines(
    map: GameInstance.MineMapUI,
    textMeasurer: TextMeasurer,
    drawConfig: MineDrawConfig,
    viewPort: Rect,
) {
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
            drawMine(item, textMeasurer, drawConfig, Offset(x * mineWidth, y * mineHeight))
        }
}

private fun DrawScope.drawMine(item: GameInstance.MineItemUI,
                               textMeasurer: TextMeasurer,
                               drawConfig: MineDrawConfig,
                               offset: Offset) {

    val paddingOffset = offset + Offset(drawConfig.padding.toPx(), drawConfig.padding.toPx())
    val outerSize = drawConfig.mineSize.toSize()
    val innerSize = drawConfig.mineInnerSize.toSize()
    if (item != GameInstance.MineItemUI.Hidden) {
        drawRoundRect(
            Color.Gray, paddingOffset, innerSize,
            CornerRadius(drawConfig.corner.toPx()), Stroke(1.dp.toPx())
        )
    }
    when (item) {
        GameInstance.MineItemUI.Hidden -> {
            drawRoundRect(drawConfig.hiddenItemColor, paddingOffset, innerSize, CornerRadius(drawConfig.corner.toPx()))
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