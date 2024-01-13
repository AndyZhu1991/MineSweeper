package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable

@Immutable
class ColorConfig(
    val schemeName: String,
    val isSystemDark: Boolean,
    val followSystem: Boolean,
    val preferLight: Boolean, // only used when followSystem is false, true for light, false for dark
) {
    fun resolveColorScheme(): ColorScheme {
        val useLightTheme = when {
            followSystem -> isSystemDark.not()
            else -> preferLight
        }
        val colorFamily = colorFamilyMap[schemeName] ?: Default
        return if (useLightTheme) {
            colorFamily.lightScheme
        } else {
            colorFamily.darkScheme
        }
    }

    companion object {
        const val defaultSchemeName = "Default" // Material3 default
        const val androidDynamicSchemeName = "Android Dynamic"

        val colorFamilies = listOf(
            Default,
            Red,
            Green,
            Blue,
            Cyan,
            Yellow,
            Magenta,
        )

        val colorNames = colorFamilies.map { it.name }
        val colorFamilyMap: Map<String, ColorFamily> = colorFamilies.associateBy { it.name }
    }
}