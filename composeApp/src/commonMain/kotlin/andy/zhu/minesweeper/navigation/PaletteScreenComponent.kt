package andy.zhu.minesweeper.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers

class PaletteScreenComponent(
    componentContext: ComponentContext,
    val onClose: () -> Unit,
    val onColorSchemeSelected: (String) -> Unit,
): ComponentContext by componentContext {
    val coroutineScope = coroutineScope(Dispatchers.Default)
}