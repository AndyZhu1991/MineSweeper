import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import andy.zhu.minesweeper.navigation.RootComponent
import andy.zhu.minesweeper.screen.AboutScreen
import andy.zhu.minesweeper.screen.LevelSelectScreen
import andy.zhu.minesweeper.screen.MainGameScreen
import andy.zhu.minesweeper.screen.PaletteScreen
import andy.zhu.minesweeper.screen.RankScreen
import andy.zhu.minesweeper.screen.SettingsScreen
import andy.zhu.minesweeper.settings.getFollowSystem
import andy.zhu.minesweeper.settings.getPreferLight
import andy.zhu.minesweeper.theme.color.ColorConfig
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun App(root: RootComponent) {
    val colorSchemeName by root.colorSchemeName.collectAsState()
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = ColorConfig(
        colorSchemeName,
        isDarkTheme,
        getFollowSystem(),
        getPreferLight(),
    ).resolveColorScheme()

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        val childStack by root.childStack.subscribeAsState()
        Surface {
            CompositionLocalProvider(LocalRippleTheme provides ThemedRippleTheme(colorScheme.primaryContainer, !isDarkTheme)) {
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

class ThemedRippleTheme(
    private val rippleColor: Color,
    private val isLight: Boolean,
) : RippleTheme {

    @Composable
    override fun defaultColor() = rippleColor

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        val rippleFactory = if (isLight) {
            rippleColor.luminance()
        } else {
            (rippleColor.luminance() + 0.8f) / 1.8f
        }
        return RippleAlpha(
            0.4f * rippleFactory,
            0.4f * rippleFactory,
            0.3f * rippleFactory,
            0.7f * rippleFactory,
        )
    }
}