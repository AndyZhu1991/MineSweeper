package andy.zhu.minesweeper.navigation

import com.arkivanov.decompose.ComponentContext

class PaletteScreenComponent(
    componentContext: ComponentContext,
    val onClose: () -> Unit,
): ComponentContext by componentContext {
}