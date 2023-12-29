package andy.zhu.minesweeper

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import andy.zhu.minesweeper.navigation.RootComponent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = retainedComponent { RootComponent(it) }
        setContent {
            App(root)
        }
    }
}
