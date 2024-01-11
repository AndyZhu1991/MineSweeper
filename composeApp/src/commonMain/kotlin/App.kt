import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import andy.zhu.minesweeper.navigation.RootComponent
import andy.zhu.minesweeper.screen.AboutScreen
import andy.zhu.minesweeper.screen.LevelSelectScreen
import andy.zhu.minesweeper.screen.MainGameScreen
import andy.zhu.minesweeper.screen.PaletteScreen
import andy.zhu.minesweeper.screen.RankScreen
import andy.zhu.minesweeper.screen.SettingsScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun App(root: RootComponent) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
        ) {
        val childStack by root.childStack.subscribeAsState()
        Surface {
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