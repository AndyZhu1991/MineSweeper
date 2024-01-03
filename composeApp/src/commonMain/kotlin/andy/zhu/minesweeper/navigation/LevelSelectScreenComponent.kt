package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameConfig
import com.arkivanov.decompose.ComponentContext
import getPlatform

class LevelSelectScreenComponent(
    componentContext: ComponentContext,
    private val startGame: (GameConfig) -> Unit,
) : ComponentContext by componentContext {
    
    fun onLevelSelected(level: GameConfig) {
        startGame(if (getPlatform().isLandscape) level.rotate() else level)
    }
}