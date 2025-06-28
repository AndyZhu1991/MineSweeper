package andy.zhu.minesweeper.game

import kotlinx.serialization.Serializable

@Serializable
class GameSave(
    val gameConfig: GameConfig,
    val mines: String,
    val opened: String,
    val flagged: String,
    val timeMillis: Int,
) {
    val mineIndices: List<Int> by lazy(LazyThreadSafetyMode.NONE) {
        splitToIntList(mines)
    }
    val openedIndices: List<Int> by lazy(LazyThreadSafetyMode.NONE) {
        splitToIntList(opened)
    }
    val flaggedIndices: List<Int> by lazy(LazyThreadSafetyMode.NONE) {
        splitToIntList(flagged)
    }

    companion object {
        fun fromGameInstance(
            config: GameConfig,
            mines: BooleanArray,
            status: Array<GameInstance.GridStatus>,
            timeMillis: Int,
        ): GameSave {
            val minePositions = mutableListOf<Int>().apply {
                mines.forEachIndexed { index, b -> if (b) add(index) }
            }
            val openedPositions = mutableListOf<Int>().apply {
                status.forEachIndexed { index, gridStatus -> if (gridStatus == GameInstance.GridStatus.OPENED) add(index) }
            }
            val flaggedPositions = mutableListOf<Int>().apply {
                status.forEachIndexed { index, gridStatus -> if (gridStatus == GameInstance.GridStatus.FLAGGED) add(index) }
            }

            return GameSave(
                config,
                minePositions.joinToString(","),
                openedPositions.joinToString(","),
                flaggedPositions.joinToString(","),
                timeMillis,
            )
        }

        private fun splitToIntList(str: String): List<Int> {
            if (str.isEmpty()) return emptyList()
            return str.split(",").mapNotNull {
                try {
                    it.toInt()
                } catch (_: Exception) {
                    null
                }
            }
        }
    }
}