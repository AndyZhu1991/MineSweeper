import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import andy.zhu.minesweeper.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun MainViewController() = ComposeUIViewController {
    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}
