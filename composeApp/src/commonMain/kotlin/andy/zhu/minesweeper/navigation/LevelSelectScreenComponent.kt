package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameLevel
import com.arkivanov.decompose.ComponentContext

class LevelSelectScreenComponent(
    componentContext: ComponentContext,
    private val startGame: (GameLevel) -> Unit,
) : ComponentContext by componentContext {
    
    fun onLevelSelected(level: GameLevel) {
        startGame(level)
    }
}