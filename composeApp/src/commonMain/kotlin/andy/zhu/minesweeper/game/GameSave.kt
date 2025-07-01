package andy.zhu.minesweeper.game

import kotlinx.serialization.Serializable

@Serializable
class GameSave(
    val gameConfig: GameConfig,
    val mineIndices: List<Int>,
    val openedIndices: List<Int>,
    val flaggedIndices: List<Int>,
    val timeMillis: Int,
) {
    fun mineIndicesStr(): String {
        return mineIndices.joinToString(",")
    }

    fun openedIndicesStr(): String {
        return openedIndices.joinToString(",")
    }

    fun flaggedIndicesStr(): String {
        return flaggedIndices.joinToString(",")
    }

    companion object {
        fun fromGameInstance(
            config: GameConfig,
            mines: BooleanArray,
            status: Array<GameInstance.GridStatus>,
            timeMillis: Int,
        ): GameSave {
            val minePositions = mines.toList().mapIndexedNotNull { index, isMine ->
                if (isMine) index else null
            }
            val openedPositions = status.mapIndexedNotNull { index, gridStatus ->
                if (gridStatus == GameInstance.GridStatus.OPENED) index else null
            }
            val flaggedPositions = status.mapIndexedNotNull { index, gridStatus ->
                if (gridStatus == GameInstance.GridStatus.FLAGGED) index else null
            }

            return GameSave(
                config,
                minePositions,
                openedPositions,
                flaggedPositions,
                timeMillis,
            )
        }
    }
}