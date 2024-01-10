package andy.zhu.minesweeper

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import andy.zhu.minesweeper.extensions.inverted
import andy.zhu.minesweeper.extensions.transform
import andy.zhu.minesweeper.game.GameInstance


internal fun DrawScope.drawMines(
    transform: Matrix,
    map: GameInstance.MineMapUI,
    animationFraction: Float,
    drawConfig: MineDrawConfig,
) {
    val invertedMatrix = transform.inverted()
    val canvasRect = Rect(Offset.Zero, size)
    val viewPort = invertedMatrix.map(canvasRect)
    val mineWidth = drawConfig.mineSize.toPx()
    val mineHeight = drawConfig.mineSize.toPx()
    val mineLeft = kotlin.math.max((viewPort.left / mineWidth).toInt(), 0)
    val mineTop = kotlin.math.max((viewPort.top / mineHeight).toInt(), 0)
    val mineRight = kotlin.math.min((viewPort.right / mineWidth).toInt(), map.width - 1)
    val mineBottom = kotlin.math.min((viewPort.bottom / mineHeight).toInt(), map.height - 1)

    drawItemBorderAndBg(transform, map, drawConfig, animationFraction, mineLeft, mineTop, mineRight, mineBottom)

    for (y in mineTop..mineBottom) {
        for (x in mineLeft..mineRight) {
            when(drawConfig) {
                is MineDrawConfig.Font -> drawMineWithFont(transform, map.getItemUI(y, x), Offset(x * mineWidth, y * mineHeight), drawConfig)
                is MineDrawConfig.Image -> drawMineWithImage(transform, map, y, x, animationFraction, drawConfig)
            }
        }
    }
}

private fun DrawScope.drawMineWithFont(
    transform: Matrix,
    item: GameInstance.MineItemUI,
    offset: Offset,
    drawConfig: MineDrawConfig.Font,
) {
    withTransform(
        transformBlock = { transform(transform) }
    ) {
        val paddingOffset = offset + Offset(drawConfig.padding.toPx(), drawConfig.padding.toPx())
        val outerSize = Size(drawConfig.mineSize.toPx(), drawConfig.mineSize.toPx())
        val innerSize = Size(drawConfig.borderRectSize.toPx(), drawConfig.borderRectSize.toPx())
        if (item != GameInstance.MineItemUI.Hidden) {
            drawRoundRect(
                Color.Gray, paddingOffset, innerSize,
                CornerRadius(drawConfig.mineCorner.toPx()), Stroke(1.dp.toPx())
            )
        }
        when (item) {
            GameInstance.MineItemUI.Hidden, GameInstance.MineItemUI.HiddenHover -> {
                drawRoundRect(drawConfig.colors.hiddenFill, paddingOffset, innerSize, CornerRadius(drawConfig.mineCorner.toPx()))
            }

            GameInstance.MineItemUI.Flagged -> {
                drawTextAtCenter(drawConfig.textMeasurer, "🚩", outerSize, drawConfig.textStyle, offset)
            }

            GameInstance.MineItemUI.Uncertain -> {
                drawTextAtCenter(drawConfig.textMeasurer, "❓", outerSize, drawConfig.textStyle, offset)
            }

            GameInstance.MineItemUI.OpenedBoom -> {
                drawTextAtCenter(drawConfig.textMeasurer, "💥", outerSize, drawConfig.textStyle, offset)
            }

            GameInstance.MineItemUI.MineView -> {
                drawTextAtCenter(drawConfig.textMeasurer, "💣", outerSize, drawConfig.textStyle, offset)
            }

            is GameInstance.MineItemUI.Opened -> {
                if (item.num != 0) {
                    drawTextAtCenter(drawConfig.textMeasurer, item.num.toString(), outerSize, drawConfig.textStyle, offset)
                }
            }

            GameInstance.MineItemUI.BlinkAnimation -> Unit
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

private fun DrawScope.drawItemBorderAndBg(
    transform: Matrix,
    map: GameInstance.MineMapUI,
    drawConfig: MineDrawConfig,
    animationFraction: Float,
    left: Int, top: Int, right: Int, bottom: Int,
) {
    withTransform(
        transformBlock = { transform(transform) }
    ) {
        val innerSize = Size(drawConfig.borderRectSize.toPx(), drawConfig.borderRectSize.toPx())
        val fillCorner = CornerRadius(drawConfig.mineCorner.toPx())
        for (y in top..bottom) { for (x in left..right) {
            val offset = Offset(x * drawConfig.mineSize.toPx(), y * drawConfig.mineSize.toPx())
            val paddingOffset = offset + Offset(drawConfig.padding.toPx(), drawConfig.padding.toPx())
            when(map.getItemUI(y, x)) {
                GameInstance.MineItemUI.Hidden -> drawConfig.colors.hiddenFill
                GameInstance.MineItemUI.HiddenHover -> drawConfig.colors.hiddenHoverFill
                GameInstance.MineItemUI.Flagged -> drawConfig.colors.markedFill
                GameInstance.MineItemUI.Uncertain -> drawConfig.colors.markedFill
                GameInstance.MineItemUI.MineView -> drawConfig.colors.markedFill
                GameInstance.MineItemUI.OpenedBoom -> drawConfig.colors.errorFill
                is GameInstance.MineItemUI.Opened -> null
                GameInstance.MineItemUI.BlinkAnimation -> {
                    if (animationFraction < 0.5f) {
                        drawConfig.colors.hiddenFill.transform(drawConfig.colors.hiddenHoverFill, animationFraction * 2)
                    } else {
                        drawConfig.colors.hiddenHoverFill.transform(drawConfig.colors.hiddenFill, (animationFraction - 0.5f) * 2)
                    }
                }
            }?.let { fillColor ->
                drawRoundRect(fillColor, paddingOffset, innerSize, fillCorner)
            }

            if (x + 1 < map.width && map.getItemUI(y, x) is GameInstance.MineItemUI.Opened
                    && map.getItemUI(y, x+1) is GameInstance.MineItemUI.Opened) {
                val start = Offset(drawConfig.mineSize.toPx(), drawConfig.mineCorner.toPx()) + offset
                val end = Offset(drawConfig.mineSize.toPx(), drawConfig.mineSize.toPx() - drawConfig.mineCorner.toPx()) + offset
                drawLine(drawConfig.colors.hiddenFill, start, end, drawConfig.borderWidth.toPx())
            }
            if (y + 1 < map.height && map.getItemUI(y, x) is GameInstance.MineItemUI.Opened
                    && map.getItemUI(y+1, x) is GameInstance.MineItemUI.Opened) {
                val start = Offset(drawConfig.mineCorner.toPx(), drawConfig.mineSize.toPx()) + offset
                val end = Offset(drawConfig.mineSize.toPx() - drawConfig.mineCorner.toPx(), drawConfig.mineSize.toPx()) + offset
                drawLine(drawConfig.colors.hiddenFill, start, end, drawConfig.borderWidth.toPx())
            }
        } }
    }
}

private fun DrawScope.drawMineWithImage(
    transform: Matrix,
    map: GameInstance.MineMapUI,
    y: Int,
    x: Int,
    animationFraction: Float,
    drawConfig: MineDrawConfig.Image,
) {
    val offset = Offset(x * drawConfig.mineSize.toPx(), y * drawConfig.mineSize.toPx())
    val paddingOffset = offset + Offset(drawConfig.padding.toPx(), drawConfig.padding.toPx())
    val item = map.getItemUI(y, x)

    val vectorOffset = paddingOffset + Offset(drawConfig.innerPadding.toPx(), drawConfig.innerPadding.toPx())
    val vectorRect = Rect(vectorOffset, Size(drawConfig.imageSize.toPx(), drawConfig.imageSize.toPx()))
    val mappedRect = transform.map(vectorRect)

    when (item) {
        GameInstance.MineItemUI.Hidden, GameInstance.MineItemUI.HiddenHover -> null
        GameInstance.MineItemUI.Flagged -> drawConfig.flagImage to drawConfig.colors.marker
        GameInstance.MineItemUI.Uncertain -> drawConfig.questionMark to drawConfig.colors.marker
        GameInstance.MineItemUI.OpenedBoom -> drawConfig.mineImage to drawConfig.colors.error
        GameInstance.MineItemUI.MineView -> drawConfig.mineImage to drawConfig.colors.mineShow
        is GameInstance.MineItemUI.Opened -> {
            if (item.num != 0) {
                drawConfig.numberImages[item.num] to drawConfig.colors.number
            } else {
                null
            }
        }
        GameInstance.MineItemUI.BlinkAnimation -> null
    }?.let { (image, color) ->
        translate(mappedRect.left, mappedRect.top) {
            with(image) {
                draw(mappedRect.size, colorFilter = ColorFilter.tint(color))
            }
        }
    }
}

data class MineCanvasColor(
    val background: Color,
    val hiddenFill: Color,
    val hiddenHoverFill: Color,
    val markedFill: Color,
    val errorFill: Color,
    val number: Color,
    val marker: Color,
    val mineShow: Color,
    val error: Color,
) {
    companion object {
        fun fromColorScheme(colorScheme: ColorScheme) = MineCanvasColor(
            background = colorScheme.background,
            hiddenFill = colorScheme.primaryContainer,
            hiddenHoverFill = colorScheme.inversePrimary,
            markedFill = colorScheme.primaryContainer.copy(alpha = 0.5f),
            errorFill = colorScheme.errorContainer,
            number = colorScheme.secondary,
            marker = colorScheme.tertiary,
            mineShow = colorScheme.primary,
            error = colorScheme.error,
        )
    }
}

sealed class MineDrawConfig(
    val colors: MineCanvasColor,
    val mineSize: Dp = 48.dp,
    val mineCorner: Dp = 3.dp,
    val padding: Dp = 1.dp,
    val borderWidth: Dp = 1.5.dp,
) {
    val borderRectSize: Dp = mineSize - padding * 2

    class Font(
        mineCanvasColor: MineCanvasColor,
        val textMeasurer: TextMeasurer,
        val textStyle: TextStyle = TextStyle(fontSize = 32.sp)
    ) : MineDrawConfig(mineCanvasColor)

    class Image(
        mineCanvasColor: MineCanvasColor,
        val numberImages: List<Painter>,
        val mineImage: Painter,
        val flagImage: Painter,
        val questionMark: Painter,
        val innerPadding: Dp = 6.dp,
    ) : MineDrawConfig(mineCanvasColor) {
        val imageSize = borderRectSize - innerPadding * 2
    }

    companion object {
        val defaultMineSize = 48.dp
        val canvasPadding = 8.dp
    }
}