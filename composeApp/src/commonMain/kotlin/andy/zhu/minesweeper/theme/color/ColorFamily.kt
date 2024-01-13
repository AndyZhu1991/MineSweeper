package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Stable

@Stable
interface ColorFamily {
    val name: String
    val lightScheme: ColorScheme
    val darkScheme: ColorScheme
}