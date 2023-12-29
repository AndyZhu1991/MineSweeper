package andy.zhu.minesweeper

import kotlinx.serialization.Serializable

@Serializable
sealed class GameLevel {
    @Serializable
    data object Low: GameLevel()
}