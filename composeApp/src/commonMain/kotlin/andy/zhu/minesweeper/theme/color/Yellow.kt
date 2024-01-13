package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Yellow: ColorFamily {
    val primaryLight = Color(0xFF616118)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFE8E78F)
    val onPrimaryContainerLight = Color(0xFF1D1D00)
    val secondaryLight = Color(0xFF606043)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFE7E4BF)
    val onSecondaryContainerLight = Color(0xFF1D1D06)
    val tertiaryLight = Color(0xFF3D6657)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFBFECD8)
    val onTertiaryContainerLight = Color(0xFF002117)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFFDF9EC)
    val onBackgroundLight = Color(0xFF1C1C14)
    val surfaceLight = Color(0xFFFDF9EC)
    val onSurfaceLight = Color(0xFF1C1C14)
    val surfaceVariantLight = Color(0xFFE6E3D1)
    val onSurfaceVariantLight = Color(0xFF48473A)
    val outlineLight = Color(0xFF797869)
    val outlineVariantLight = Color(0xFFCAC7B6)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF313128)
    val inverseOnSurfaceLight = Color(0xFFF4F1E3)
    val inversePrimaryLight = Color(0xFFCBCB76)

    val primaryDark = Color(0xFFCBCB76)
    val onPrimaryDark = Color(0xFF323200)
    val primaryContainerDark = Color(0xFF494900)
    val onPrimaryContainerDark = Color(0xFFE8E78F)
    val secondaryDark = Color(0xFFCAC8A5)
    val onSecondaryDark = Color(0xFF323218)
    val secondaryContainerDark = Color(0xFF49482D)
    val onSecondaryContainerDark = Color(0xFFE7E4BF)
    val tertiaryDark = Color(0xFFA4D0BD)
    val onTertiaryDark = Color(0xFF0B372A)
    val tertiaryContainerDark = Color(0xFF254E40)
    val onTertiaryContainerDark = Color(0xFFBFECD8)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF14140C)
    val onBackgroundDark = Color(0xFFE6E3D5)
    val surfaceDark = Color(0xFF14140C)
    val onSurfaceDark = Color(0xFFE6E3D5)
    val surfaceVariantDark = Color(0xFF48473A)
    val onSurfaceVariantDark = Color(0xFFCAC7B6)
    val outlineDark = Color(0xFF939182)
    val outlineVariantDark = Color(0xFF48473A)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE6E3D5)
    val inverseOnSurfaceDark = Color(0xFF313128)
    val inversePrimaryDark = Color(0xFF616118)

    override val name: String = "Yellow"

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