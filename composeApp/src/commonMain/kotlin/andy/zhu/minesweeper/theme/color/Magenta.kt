package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Magenta: ColorFamily {
    val primaryLight = Color(0xFF804D7A)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFFFD7F5)
    val onPrimaryContainerLight = Color(0xFF340832)
    val secondaryLight = Color(0xFF6E5869)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFF7DAEF)
    val onSecondaryContainerLight = Color(0xFF271624)
    val tertiaryLight = Color(0xFF825345)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFFFDBD1)
    val onTertiaryContainerLight = Color(0xFF321208)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFFFF7F9)
    val onBackgroundLight = Color(0xFF201A1E)
    val surfaceLight = Color(0xFFFFF7F9)
    val onSurfaceLight = Color(0xFF201A1E)
    val surfaceVariantLight = Color(0xFFEEDEE7)
    val onSurfaceVariantLight = Color(0xFF4E444B)
    val outlineLight = Color(0xFF80747C)
    val outlineVariantLight = Color(0xFFD1C2CB)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF352E33)
    val inverseOnSurfaceLight = Color(0xFFFAEDF4)
    val inversePrimaryLight = Color(0xFFF1B3E6)

    val primaryDark = Color(0xFFF1B3E6)
    val onPrimaryDark = Color(0xFF4C1F49)
    val primaryContainerDark = Color(0xFF653661)
    val onPrimaryContainerDark = Color(0xFFFFD7F5)
    val secondaryDark = Color(0xFFDABFD2)
    val onSecondaryDark = Color(0xFF3D2B3A)
    val secondaryContainerDark = Color(0xFF554151)
    val onSecondaryContainerDark = Color(0xFFF7DAEF)
    val tertiaryDark = Color(0xFFF5B8A7)
    val onTertiaryDark = Color(0xFF4C261B)
    val tertiaryContainerDark = Color(0xFF663C2F)
    val onTertiaryContainerDark = Color(0xFFFFDBD1)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF171216)
    val onBackgroundDark = Color(0xFFECDFE5)
    val surfaceDark = Color(0xFF171216)
    val onSurfaceDark = Color(0xFFECDFE5)
    val surfaceVariantDark = Color(0xFF4E444B)
    val onSurfaceVariantDark = Color(0xFFD1C2CB)
    val outlineDark = Color(0xFF9A8D95)
    val outlineVariantDark = Color(0xFF4E444B)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFECDFE5)
    val inverseOnSurfaceDark = Color(0xFF352E33)
    val inversePrimaryDark = Color(0xFF804D7A)

    override val name: String = "Magenta"

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