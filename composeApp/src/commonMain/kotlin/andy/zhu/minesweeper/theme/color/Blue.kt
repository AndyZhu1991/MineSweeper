package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Blue: ColorFamily {
    val primaryLight = Color(0xFF555992)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFE0E0FF)
    val onPrimaryContainerLight = Color(0xFF11144B)
    val secondaryLight = Color(0xFF5C5D72)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFE1E0F9)
    val onSecondaryContainerLight = Color(0xFF191A2C)
    val tertiaryLight = Color(0xFF78536B)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFFFD8EE)
    val onTertiaryContainerLight = Color(0xFF2E1126)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFFBF8FF)
    val onBackgroundLight = Color(0xFF1B1B21)
    val surfaceLight = Color(0xFFFBF8FF)
    val onSurfaceLight = Color(0xFF1B1B21)
    val surfaceVariantLight = Color(0xFFE4E1EC)
    val onSurfaceVariantLight = Color(0xFF46464F)
    val outlineLight = Color(0xFF777680)
    val outlineVariantLight = Color(0xFFC7C5D0)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF303036)
    val inverseOnSurfaceLight = Color(0xFFF2EFF7)
    val inversePrimaryLight = Color(0xFFBEC2FF)

    val primaryDark = Color(0xFFBEC2FF)
    val onPrimaryDark = Color(0xFF272B60)
    val primaryContainerDark = Color(0xFF3E4278)
    val onPrimaryContainerDark = Color(0xFFE0E0FF)
    val secondaryDark = Color(0xFFC5C4DD)
    val onSecondaryDark = Color(0xFF2E2F42)
    val secondaryContainerDark = Color(0xFF444559)
    val onSecondaryContainerDark = Color(0xFFE1E0F9)
    val tertiaryDark = Color(0xFFE8B9D5)
    val onTertiaryDark = Color(0xFF46263B)
    val tertiaryContainerDark = Color(0xFF5E3C52)
    val onTertiaryContainerDark = Color(0xFFFFD8EE)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF131318)
    val onBackgroundDark = Color(0xFFE4E1E9)
    val surfaceDark = Color(0xFF131318)
    val onSurfaceDark = Color(0xFFE4E1E9)
    val surfaceVariantDark = Color(0xFF46464F)
    val onSurfaceVariantDark = Color(0xFFC7C5D0)
    val outlineDark = Color(0xFF91909A)
    val outlineVariantDark = Color(0xFF46464F)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE4E1E9)
    val inverseOnSurfaceDark = Color(0xFF303036)
    val inversePrimaryDark = Color(0xFF555992)

    override val name: String = "Blue"

    override val lightScheme = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
    )

    override val darkScheme = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
    )
}