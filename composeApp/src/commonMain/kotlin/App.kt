import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import andy.zhu.minesweeper.navigation.RootComponent
import andy.zhu.minesweeper.screen.LevelSelectScreen
import andy.zhu.minesweeper.screen.MainGameScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun App(root: RootComponent) {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        val childStack by root.childStack.subscribeAsState()
        Children(
            stack = childStack,
            animation = stackAnimation(slide()),
        ) { child ->
            when(val instance = child.instance) {
                is RootComponent.Child.LevelSelectScreen -> LevelSelectScreen(instance.component)
                is RootComponent.Child.MainGameScreen -> MainGameScreen(instance.component)
            }
        }
    }
}