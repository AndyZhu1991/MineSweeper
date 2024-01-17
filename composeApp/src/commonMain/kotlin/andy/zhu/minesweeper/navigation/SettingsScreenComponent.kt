package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.settings.getDefaultAction
import andy.zhu.minesweeper.settings.getShowActionToggle
import andy.zhu.minesweeper.settings.saveDefaultAction
import andy.zhu.minesweeper.settings.saveShowActionToggle
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class SettingsScreenComponent(
    componentContext: ComponentContext,
    val onClose: () -> Unit,
): ComponentContext by componentContext {

    val coroutineScope = coroutineScope()

    private val _showActionToggle = MutableStateFlow(getShowActionToggle())
    val showActionToggle: StateFlow<Boolean> = _showActionToggle

    private val _defaultAction = MutableStateFlow(getDefaultAction())
    val defaultAction: StateFlow<DefaultAction> = _defaultAction

    fun onShowActionToggleChanged(showActionToggle: Boolean) {
        _showActionToggle.value = showActionToggle
        saveShowActionToggle(showActionToggle)
    }

    fun onDefaultActionChanged(defaultAction: DefaultAction) {
        _defaultAction.value = defaultAction
        saveDefaultAction(defaultAction)
    }

    @Serializable
    enum class DefaultAction {
        Flag, Dig
    }
}