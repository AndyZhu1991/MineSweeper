package andy.zhu.minesweeper.screen

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import andy.zhu.minesweeper.navigation.PaletteScreenComponent

@Composable
fun PaletteScreen(component: PaletteScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Palette",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
    }
}