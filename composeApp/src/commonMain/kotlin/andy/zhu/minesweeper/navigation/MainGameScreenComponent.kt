package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameInstance
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.settings.removeGameSave
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class MainGameScreenComponent(
    componentContext: ComponentContext,
    val onClose: () -> Unit,
    gameInstanceInitializer: (CoroutineScope) -> GameInstance,
) : ComponentContext by componentContext {
    private val coroutineScope: CoroutineScope = coroutineScope(Dispatchers.Default)
    val gameInstance: MutableStateFlow<GameInstance>
    val gameConfig: GameConfig

    constructor(
        componentContext: ComponentContext,
        gameConfig: GameConfig,
        onClose: () -> Unit,
    ): this(componentContext, onClose, { GameInstance(gameConfig, it) })

    constructor(
        componentContext: ComponentContext,
        gameSave: GameSave,
        onClose: () -> Unit
    ): this(componentContext, onClose, { GameInstance.fromGameSave(gameSave, it) })

    init {
        this.gameInstance = MutableStateFlow(gameInstanceInitializer(coroutineScope))
        this.gameConfig = gameInstance.value.gameConfig
        doOnResume { gameInstance.value.onResume() }
        doOnPause { gameInstance.value.onPause() }
        doOnDestroy(::saveGame)
    }

    private fun saveGame() {
        if (!gameInstance.value.gameStarted.value || gameInstance.value.gameEnd.value) {
            removeGameSave(gameInstance.value.gameConfig.level)
        } else {
            andy.zhu.minesweeper.settings.saveGame(
                gameInstance.value.gameConfig.level, gameInstance.value.save())
        }
    }

    fun onRefresh() {
        removeGameSave(gameInstance.value.gameConfig.level)
        gameInstance.value.onDestroy()
        gameInstance.value = GameInstance(gameConfig, coroutineScope)
        Napier.i("Game refreshed.")
    }
}