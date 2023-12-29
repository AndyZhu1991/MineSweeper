import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import andy.zhu.minesweeper.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "MineSweeper") {
        val root = remember {
            RootComponent(DefaultComponentContext(LifecycleRegistry()))
        }
        App(root)
    }
}
