package andy.zhu.minesweeper

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.screen.MineDrawConfig


internal fun DrawScope.drawMines(map: GameInstance.MineMapUI, textMeasurer: TextMeasurer, drawConfig: MineDrawConfig) {
    map.enumeratedItems.forEach { (y, x, item) ->
        drawMine(item, textMeasurer, drawConfig,
                 Offset(x * drawConfig.mineSize.width.toPx(), y * drawConfig.mineSize.height.toPx()))
    }
}

private fun DrawScope.drawMine(item: GameInstance.MineItemUI,
                               textMeasurer: TextMeasurer,
                               drawConfig: MineDrawConfig,
                               offset: Offset) {
    withTransform(
        transformBlock = { translate(offset.x, offset.y) },
        drawBlock = {
            val paddingOffset = Offset(drawConfig.padding.toPx(), drawConfig.padding.toPx())
            val outerSize = drawConfig.mineSize.toSize()
            val innerSize = drawConfig.mineInnerSize.toSize()
            if (item != GameInstance.MineItemUI.Hidden) {
                drawRoundRect(Color.Gray, paddingOffset, innerSize,
                              CornerRadius(drawConfig.corner.toPx()), Stroke(1.dp.toPx()))
            }
            when(item) {
                GameInstance.MineItemUI.Hidden -> {
                    drawRoundRect(Color.Cyan, paddingOffset, innerSize, CornerRadius(drawConfig.corner.toPx()))
                }
                GameInstance.MineItemUI.Flagged -> {
                    drawTextAtCenter(textMeasurer, "🚩", outerSize, drawConfig.itemTextStyle)
                }
                GameInstance.MineItemUI.Uncertain -> {
                    drawTextAtCenter(textMeasurer, "❓", outerSize, drawConfig.itemTextStyle)
                }
                GameInstance.MineItemUI.OpenedBoom -> {
                    drawTextAtCenter(textMeasurer, "💥", outerSize, drawConfig.itemTextStyle)
                }
                GameInstance.MineItemUI.MineView -> {
                    drawTextAtCenter(textMeasurer, "💣", outerSize, drawConfig.itemTextStyle)
                }
                is GameInstance.MineItemUI.Opened -> {
                    if (item.num != 0) {
                        drawTextAtCenter(textMeasurer, item.num.toString(), outerSize, drawConfig.itemTextStyle)
                    }
                }
            }
        }
    )
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
        )
    )
}