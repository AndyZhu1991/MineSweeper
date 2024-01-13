package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

object Default: ColorFamily {
    override val name: String = ColorConfig.defaultSchemeName

    override val lightScheme = lightColorScheme()

    override val darkScheme = darkColorScheme()
}