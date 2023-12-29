import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import andy.zhu.minesweeper.navigation.RootComponent
import andy.zhu.minesweeper.screen.LevelSelectScreen
import andy.zhu.minesweeper.screen.MainGameScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(root: RootComponent) {
    MaterialTheme {
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