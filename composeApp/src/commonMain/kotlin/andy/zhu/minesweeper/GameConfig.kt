package andy.zhu.minesweeper

import kotlinx.serialization.Serializable

@Serializable
class GameConfig(
    val width: Int,
    val height: Int,
    val mineCount: Int,
    val level: Level,
    val shortDesc: String,
) {
    fun name() = level.name

    fun mapSize() = width * height

    fun rotate() = GameConfig(height, width, mineCount, level, shortDesc)

    enum class Level {
        Easy, Medium, Hard, Extreme, Custom
    }

    companion object {
        val Easy = GameConfig(
            10, 10, 10, Level.Easy,
            "Beginner-friendly, fewer mines."
        )

        val Medium = GameConfig(
            18, 14, 40, Level.Medium,
            "Intermediate level, more mines."
        )

        val Hard = GameConfig(
            30, 16, 99, Level.Hard,
            "Advanced, densely mined."
        )

        val Extreme = GameConfig(
            50, 30, 400, Level.Extreme,
            "Expert level, extreme mines."
        )

        fun Custom(width: Int, height: Int, count: Int) = GameConfig(
            width, height, count, Level.Custom,
            "Create your minefield."
        )
    }
}