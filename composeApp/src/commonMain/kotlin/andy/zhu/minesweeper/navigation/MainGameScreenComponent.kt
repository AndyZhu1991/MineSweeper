package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.GameConfig
import andy.zhu.minesweeper.GameInstance
import com.arkivanov.decompose.ComponentContext

class MainGameScreenComponent(
    componentContext: ComponentContext,
    val level: GameConfig,
) : ComponentContext by componentContext {
    val gameInstance = GameInstance(level)
}