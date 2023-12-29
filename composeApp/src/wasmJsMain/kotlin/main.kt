import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import andy.zhu.minesweeper.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        val lifecycle = LifecycleRegistry()
        val root = remember {
            RootComponent(DefaultComponentContext(lifecycle))
        }
        App(root)
    }
}