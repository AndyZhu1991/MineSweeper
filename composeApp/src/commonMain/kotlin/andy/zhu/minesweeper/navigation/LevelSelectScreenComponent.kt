package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import com.arkivanov.decompose.ComponentContext
import getPlatform

class LevelSelectScreenComponent(
    componentContext: ComponentContext,
    private val startGame: (GameConfig) -> Unit,
    private val resumeGame: (GameSave) -> Unit,
) : ComponentContext by componentContext {
    
    fun onLevelSelected(level: GameConfig) {
        startGame(if (getPlatform().isLandscape) level.rotate() else level)
    }

    fun onGameResume(gameSave: GameSave) {
        resumeGame(gameSave)
    }
}