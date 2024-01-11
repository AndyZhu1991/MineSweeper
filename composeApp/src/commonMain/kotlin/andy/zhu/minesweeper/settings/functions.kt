package andy.zhu.minesweeper.settings

import andy.zhu.minesweeper.game.GameConfig
import getPlatform

fun makeRankKey(config: GameConfig) = "rank_${config.name()}"

fun getRank(config: GameConfig): List<RankItem> {
    return getPlatform().settings.getObject(makeRankKey(config), emptyList())
}

fun saveRank(config: GameConfig, rank: List<RankItem>) {
    getPlatform().settings.putObject(makeRankKey(config), rank)
}