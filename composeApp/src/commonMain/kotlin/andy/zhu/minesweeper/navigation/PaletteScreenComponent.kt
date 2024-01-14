package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.theme.color.ColorPreference
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers

class PaletteScreenComponent(
    componentContext: ComponentContext,
    val onClose: () -> Unit,
    val onColorSchemeSelected: (String) -> Unit,
    val onColorPreferenceSelected: (ColorPreference) -> Unit,
): ComponentContext by componentContext {
    val coroutineScope = coroutineScope(Dispatchers.Default)
}
