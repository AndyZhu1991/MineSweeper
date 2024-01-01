package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameConfig
import andy.zhu.minesweeper.GameInstance
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow

class MainGameScreenComponent(
    componentContext: ComponentContext,
    val level: GameConfig,
    val onClose: () -> Unit,
) : ComponentContext by componentContext {
    val gameInstance = MutableStateFlow<GameInstance>(GameInstance(level))

    init {
        doOnDestroy { gameInstance.value.destroy() }
    }

    fun onRefresh() {
        gameInstance.value.destroy()
        gameInstance.value = GameInstance(level)
        Napier.i("Game refreshed.")
    }
}