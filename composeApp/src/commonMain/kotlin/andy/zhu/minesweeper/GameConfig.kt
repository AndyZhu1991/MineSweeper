package andy.zhu.minesweeper

import kotlinx.serialization.Serializable

@Serializable
sealed class GameConfig(
    val width: Int,
    val height: Int,
    val mineCount: Int,
) {
    fun mapSize() = width * height

    fun rotate(): GameConfig {
        return Custom(height, width, mineCount)
    }

    @Serializable
    data object Easy: GameConfig(10, 10, 10)

    @Serializable
    data object Medium: GameConfig(21, 14, 30)

    @Serializable
    data object Hard: GameConfig(30, 20, 80)

    @Serializable
    data class Custom(
        private val w: Int,
        private val h: Int,
        private val c: Int,
    ): GameConfig(w, h, c)
}