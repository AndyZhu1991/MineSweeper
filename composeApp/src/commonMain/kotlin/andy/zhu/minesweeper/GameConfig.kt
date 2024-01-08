package andy.zhu.minesweeper

import kotlinx.serialization.Serializable

@Serializable
sealed class GameConfig(
    val width: Int,
    val height: Int,
    val mineCount: Int,
    val levelName: String,
) {
    fun mapSize() = width * height

    fun rotate(): GameConfig {
        return Custom(height, width, mineCount)
    }

    @Serializable
    data object Easy: GameConfig(10, 10, 10, "Easy")

    @Serializable
    data object Medium: GameConfig(21, 14, 30, "Medium")

    @Serializable
    data object Hard: GameConfig(30, 20, 80, "Hard")

    @Serializable
    data class Custom(
        private val w: Int,
        private val h: Int,
        private val c: Int,
    ): GameConfig(w, h, c, "Custom")
}