package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameLevel
import com.arkivanov.decompose.ComponentContext

class MainGameScreenComponent(
    componentContext: ComponentContext,
    val level: GameLevel,
) : ComponentContext by componentContext {
    
}