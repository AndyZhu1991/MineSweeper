package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameConfig
import andy.zhu.minesweeper.GameInstance
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class MainGameScreenComponent(
    componentContext: ComponentContext,
    val level: GameConfig,
    val onClose: () -> Unit,
) : ComponentContext by componentContext {
    private val coroutineScrop = coroutineScope(Dispatchers.Default)
    val gameInstance = MutableStateFlow(GameInstance(level, coroutineScrop))

    init {
        doOnResume { gameInstance.value.onResume() }
        doOnPause { gameInstance.value.onPause() }
    }

    fun onRefresh() {
        gameInstance.value.onDestroy()
        gameInstance.value = GameInstance(level, coroutineScrop)
        Napier.i("Game refreshed.")
    }
}