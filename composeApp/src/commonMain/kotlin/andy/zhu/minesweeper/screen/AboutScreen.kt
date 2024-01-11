package andy.zhu.minesweeper.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import andy.zhu.minesweeper.navigation.AboutScreenComponent

@Composable
fun AboutScreen(component: AboutScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("About",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
    }
}