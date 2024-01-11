package andy.zhu.minesweeper.settings

import kotlinx.serialization.Serializable

@Serializable
class RankItem(
    val timeStampMillis: Long,
    val costTimeMillis: Long,
)