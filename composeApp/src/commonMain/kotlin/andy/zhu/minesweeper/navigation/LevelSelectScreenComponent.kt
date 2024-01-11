package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import com.arkivanov.decompose.ComponentContext
import getPlatform

class LevelSelectScreenComponent(
    componentContext: ComponentContext,
    val navigation: Navigation,
) : ComponentContext by componentContext {
    
    fun onLevelSelected(level: GameConfig) {
        navigation.startGame(if (getPlatform().isLandscape) level.rotate() else level)
    }

    fun onGameResume(gameSave: GameSave) {
        navigation.resumeGame(gameSave)
    }

    interface Navigation {
        fun startGame(level: GameConfig)
        fun resumeGame(gameSave: GameSave)
        fun settings()
        fun palette()
        fun rank(currentLevel: GameConfig.Level)
        fun about()
    }
}