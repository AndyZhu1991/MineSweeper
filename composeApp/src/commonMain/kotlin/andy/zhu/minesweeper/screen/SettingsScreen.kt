package andy.zhu.minesweeper.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import andy.zhu.minesweeper.navigation.SettingsScreenComponent

@Composable
fun SettingsScreen(component: SettingsScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Settings",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
    }
}