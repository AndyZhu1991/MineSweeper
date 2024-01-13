package andy.zhu.minesweeper.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Red: ColorFamily {
    val primaryLight = Color(0xFF904B40)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFFFDAD4)
    val onPrimaryContainerLight = Color(0xFF3A0905)
    val secondaryLight = Color(0xFF775651)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFFFDAD4)
    val onSecondaryContainerLight = Color(0xFF2C1512)
    val tertiaryLight = Color(0xFF705C2E)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFFBDFA6)
    val onTertiaryContainerLight = Color(0xFF251A00)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF410002)
    val backgroundLight = Color(0xFFFFF8F6)
    val onBackgroundLight = Color(0xFF231918)
    val surfaceLight = Color(0xFFFFF8F6)
    val onSurfaceLight = Color(0xFF231918)
    val surfaceVariantLight = Color(0xFFF5DDDA)
    val onSurfaceVariantLight = Color(0xFF534341)
    val outlineLight = Color(0xFF857370)
    val outlineVariantLight = Color(0xFFD8C2BE)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF392E2C)
    val inverseOnSurfaceLight = Color(0xFFFFEDEA)
    val inversePrimaryLight = Color(0xFFFFB4A8)

    val primaryDark = Color(0xFFFFB4A8)
    val onPrimaryDark = Color(0xFF561E16)
    val primaryContainerDark = Color(0xFF73342A)
    val onPrimaryContainerDark = Color(0xFFFFDAD4)
    val secondaryDark = Color(0xFFE7BDB6)
    val onSecondaryDark = Color(0xFF442925)
    val secondaryContainerDark = Color(0xFF5D3F3B)
    val onSecondaryContainerDark = Color(0xFFFFDAD4)
    val tertiaryDark = Color(0xFFDEC48C)
    val onTertiaryDark = Color(0xFF3E2E04)
    val tertiaryContainerDark = Color(0xFF564419)
    val onTertiaryContainerDark = Color(0xFFFBDFA6)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF1A1110)
    val onBackgroundDark = Color(0xFFF1DFDC)
    val surfaceDark = Color(0xFF1A1110)
    val onSurfaceDark = Color(0xFFF1DFDC)
    val surfaceVariantDark = Color(0xFF534341)
    val onSurfaceVariantDark = Color(0xFFD8C2BE)
    val outlineDark = Color(0xFFA08C89)
    val outlineVariantDark = Color(0xFF534341)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFF1DFDC)
    val inverseOnSurfaceDark = Color(0xFF392E2C)
    val inversePrimaryDark = Color(0xFF904B40)

    override val name: String = "Red"

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