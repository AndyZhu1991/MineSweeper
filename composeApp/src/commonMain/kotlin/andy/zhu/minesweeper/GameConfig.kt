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

    abstract fun shortDesc(): String

    @Serializable
    data object Easy: GameConfig(10, 10, 10, "Easy") {
        override fun shortDesc(): String  = "Beginner-friendly, fewer mines."
    }

    @Serializable
    data object Medium: GameConfig(18, 14, 40, "Medium") {
        override fun shortDesc(): String = "Intermediate level, more mines."
    }

    @Serializable
    data object Hard: GameConfig(30, 16, 99, "Hard") {
        override fun shortDesc(): String = "Advanced, densely mined."
    }

    @Serializable
    data object Extreme: GameConfig(50, 30, 400, "Extreme") {
        override fun shortDesc(): String = "Expert level, extreme mines."
    }

    @Serializable
    data class Custom(
        private val w: Int,
        private val h: Int,
        private val c: Int,
    ): GameConfig(w, h, c, "Custom") {
        override fun shortDesc(): String = "Create your minefield."
    }
}