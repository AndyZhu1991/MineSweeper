package andy.zhu.minesweeper

import kotlinx.serialization.Serializable

@Serializable
sealed class GameConfig(
    val width: Int,
    val height: Int,
    val mineCount: Int,
) {
    fun mapSize() = width * height
    
    @Serializable
    data object Low: GameConfig(10, 10, 10)
}