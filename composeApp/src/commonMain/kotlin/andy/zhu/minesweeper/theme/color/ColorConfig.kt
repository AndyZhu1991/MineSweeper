package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
class ColorConfig(
    val schemeName: String,
    val isSystemDark: Boolean,
    val colorPreference: ColorPreference,
) {
    fun resolveColorScheme(): ColorScheme {
        val useLightTheme = when(colorPreference) {
            ColorPreference.FollowSystem -> !isSystemDark
            ColorPreference.Light -> true
            ColorPreference.Dark -> false
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

@Serializable
enum class ColorPreference {
    FollowSystem, Light, Dark
}