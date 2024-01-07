package andy.zhu.minesweeper.settings

import kotlinx.serialization.Serializable

@Serializable
data class RecordItem(
    val timeStampMillis: Long,
    val costTimeMillis: Long,
)