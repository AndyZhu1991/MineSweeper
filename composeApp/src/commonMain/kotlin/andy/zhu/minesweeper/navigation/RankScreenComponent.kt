package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import com.arkivanov.decompose.ComponentContext

class RankScreenComponent(
    componentContext: ComponentContext,
    val currentLevel: GameConfig.Level,
    val onClose: () -> Unit,
): ComponentContext by componentContext