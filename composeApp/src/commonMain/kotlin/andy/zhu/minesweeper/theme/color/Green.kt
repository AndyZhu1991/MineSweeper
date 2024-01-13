package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Green: ColorFamily {
    val primaryLight = Color(0xFF406836)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFC0EFB0)
    val onPrimaryContainerLight = Color(0xFF002200)
    val secondaryLight = Color(0xFF54634D)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFD7E8CD)
    val onSecondaryContainerLight = Color(0xFF121F0E)
    val tertiaryLight = Color(0xFF386568)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFBCEBEE)
    val onTertiaryContainerLight = Color(0xFF002022)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFF8FBF1)
    val onBackgroundLight = Color(0xFF191D17)
    val surfaceLight = Color(0xFFF8FBF1)
    val onSurfaceLight = Color(0xFF191D17)
    val surfaceVariantLight = Color(0xFFDFE4D7)
    val onSurfaceVariantLight = Color(0xFF43483F)
    val outlineLight = Color(0xFF73796E)
    val outlineVariantLight = Color(0xFFC3C8BC)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF2E322B)
    val inverseOnSurfaceLight = Color(0xFFEFF2E8)
    val inversePrimaryLight = Color(0xFFA5D395)

    val primaryDark = Color(0xFFA5D395)
    val onPrimaryDark = Color(0xFF11380B)
    val primaryContainerDark = Color(0xFF285020)
    val onPrimaryContainerDark = Color(0xFFC0EFB0)
    val secondaryDark = Color(0xFFBBCBB2)
    val onSecondaryDark = Color(0xFF263422)
    val secondaryContainerDark = Color(0xFF3C4B37)
    val onSecondaryContainerDark = Color(0xFFD7E8CD)
    val tertiaryDark = Color(0xFFA0CFD2)
    val onTertiaryDark = Color(0xFF003739)
    val tertiaryContainerDark = Color(0xFF1E4D50)
    val onTertiaryContainerDark = Color(0xFFBCEBEE)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF11140F)
    val onBackgroundDark = Color(0xFFE1E4DA)
    val surfaceDark = Color(0xFF11140F)
    val onSurfaceDark = Color(0xFFE1E4DA)
    val surfaceVariantDark = Color(0xFF43483F)
    val onSurfaceVariantDark = Color(0xFFC3C8BC)
    val outlineDark = Color(0xFF8D9387)
    val outlineVariantDark = Color(0xFF43483F)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE1E4DA)
    val inverseOnSurfaceDark = Color(0xFF2E322B)
    val inversePrimaryDark = Color(0xFF406836)

    override val name: String = "Green"

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