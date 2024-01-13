package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Cyan: ColorFamily {
    val primaryLight = Color(0xFF006A6A)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFF9CF1F0)
    val onPrimaryContainerLight = Color(0xFF002020)
    val secondaryLight = Color(0xFF4A6363)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFCCE8E7)
    val onSecondaryContainerLight = Color(0xFF051F1F)
    val tertiaryLight = Color(0xFF4B607C)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFD3E4FF)
    val onTertiaryContainerLight = Color(0xFF041C35)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFF4FBFA)
    val onBackgroundLight = Color(0xFF161D1D)
    val surfaceLight = Color(0xFFF4FBFA)
    val onSurfaceLight = Color(0xFF161D1D)
    val surfaceVariantLight = Color(0xFFDAE5E4)
    val onSurfaceVariantLight = Color(0xFF3F4948)
    val outlineLight = Color(0xFF6F7979)
    val outlineVariantLight = Color(0xFFBEC9C8)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF2B3231)
    val inverseOnSurfaceLight = Color(0xFFECF2F1)
    val inversePrimaryLight = Color(0xFF80D5D4)

    val primaryDark = Color(0xFF80D5D4)
    val onPrimaryDark = Color(0xFF003737)
    val primaryContainerDark = Color(0xFF004F4F)
    val onPrimaryContainerDark = Color(0xFF9CF1F0)
    val secondaryDark = Color(0xFFB0CCCB)
    val onSecondaryDark = Color(0xFF1B3534)
    val secondaryContainerDark = Color(0xFF324B4B)
    val onSecondaryContainerDark = Color(0xFFCCE8E7)
    val tertiaryDark = Color(0xFFB3C8E8)
    val onTertiaryDark = Color(0xFF1C314B)
    val tertiaryContainerDark = Color(0xFF334863)
    val onTertiaryContainerDark = Color(0xFFD3E4FF)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF0E1514)
    val onBackgroundDark = Color(0xFFDDE4E3)
    val surfaceDark = Color(0xFF0E1514)
    val onSurfaceDark = Color(0xFFDDE4E3)
    val surfaceVariantDark = Color(0xFF3F4948)
    val onSurfaceVariantDark = Color(0xFFBEC9C8)
    val outlineDark = Color(0xFF889392)
    val outlineVariantDark = Color(0xFF3F4948)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFDDE4E3)
    val inverseOnSurfaceDark = Color(0xFF2B3231)
    val inversePrimaryDark = Color(0xFF006A6A)

    override val name: String = "Cyan"

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