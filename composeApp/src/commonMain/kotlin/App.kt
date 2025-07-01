import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.luminance
import andy.zhu.minesweeper.database.dbModule
import andy.zhu.minesweeper.navigation.RootComponent
import andy.zhu.minesweeper.screen.AboutScreen
import andy.zhu.minesweeper.screen.LevelSelectScreen
import andy.zhu.minesweeper.screen.MainGameScreen
import andy.zhu.minesweeper.screen.PaletteScreen
import andy.zhu.minesweeper.screen.RankScreen
import andy.zhu.minesweeper.screen.SettingsScreen
import andy.zhu.minesweeper.theme.color.ColorConfig
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.koin.compose.KoinApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(root: RootComponent) {
    val colorSchemeName by root.colorSchemeName.collectAsState()
    val colorPreference by root.colorPreference.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val colorConfig = ColorConfig(
        colorSchemeName,
        isSystemDark,
        colorPreference,
    )
    val colorScheme = colorConfig.resolveColorScheme()

    LaunchedEffect(colorConfig.useLightTheme()) {
        root.setStatusBarDark(!colorConfig.useLightTheme())
    }

    KoinApplication(
        application = {
            modules(dbModule)
        }
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            val childStack by root.childStack.subscribeAsState()
            Surface {
                CompositionLocalProvider(
                    value = LocalRippleConfiguration provides createRippleConfiguration(colorConfig.useLightTheme(), colorScheme)
                ) {
                    Children(
                        stack = childStack,
                        animation = stackAnimation(slide()),
                    ) { child ->
                        when(val instance = child.instance) {
                            is RootComponent.Child.LevelSelectScreen -> LevelSelectScreen(instance.component)
                            is RootComponent.Child.MainGameScreen -> MainGameScreen(instance.component)
                            is RootComponent.Child.AboutScreen -> AboutScreen(instance.component)
                            is RootComponent.Child.PaletteScreen -> PaletteScreen(instance.component)
                            is RootComponent.Child.RankScreen -> RankScreen(instance.component)
                            is RootComponent.Child.SettingsScreen -> SettingsScreen(instance.component)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun createRippleConfiguration(useLightTheme: Boolean, colorScheme: ColorScheme): RippleConfiguration {
    val rippleColor = colorScheme.primaryContainer
    val rippleFactory = if (useLightTheme) {
        rippleColor.luminance()
    } else {
        (rippleColor.luminance() + 0.8f) / 1.8f
    }
    return RippleConfiguration(
        color = rippleColor,
        rippleAlpha = RippleAlpha(
            pressedAlpha = 0.4f * rippleFactory,
            focusedAlpha = 0.4f * rippleFactory,
            draggedAlpha = 0.3f * rippleFactory,
            hoveredAlpha = 0.7f * rippleFactory,
        )
    )
}