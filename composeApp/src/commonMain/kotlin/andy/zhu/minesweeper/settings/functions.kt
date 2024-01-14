package andy.zhu.minesweeper.settings

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.theme.color.ColorConfig
import andy.zhu.minesweeper.theme.color.ColorPreference
import getPlatform

/**************************************************************************************************/
// Rank
/**************************************************************************************************/

private const val PREF_NAME_RANK = "rank"

fun getRank(config: GameConfig): List<RankItem> {
    return getPlatform().getPreference(PREF_NAME_RANK).getObject(config.name(), emptyList())
}

fun saveRank(config: GameConfig, rank: List<RankItem>) {
    getPlatform().getPreference(PREF_NAME_RANK).putObject(config.name(), rank)
}


/**************************************************************************************************/
// Game save
/**************************************************************************************************/

private const val PREF_NAME_GAME_SAVE = "game_save"

fun saveGame(level: GameConfig.Level, gameSave: GameSave) {
    getPlatform().getPreference(PREF_NAME_GAME_SAVE).putObject(level.name, gameSave)
}

fun loadGame(level: GameConfig.Level): GameSave? {
    return getPlatform().getPreference(PREF_NAME_GAME_SAVE).getObjectOrNull(level.name)
}

fun removeGameSave(level: GameConfig.Level) {
    getPlatform().getPreference(PREF_NAME_GAME_SAVE).remove(level.name)
}


/**************************************************************************************************/
// Color scheme
/**************************************************************************************************/

private const val PREF_NAME_THEME = "theme"
private const val PREF_KEY_COLOR_SCHEME_NAME = "color_scheme_name"
private const val PREF_KEY_COLOR_PREFERENCE = "color_preference"

fun getColorSchemeName(): String {
    return getPlatform().getPreference(PREF_NAME_THEME)
        .getString(PREF_KEY_COLOR_SCHEME_NAME, ColorConfig.defaultSchemeName)
}

fun saveColorSchemeName(schemeName: String) {
    getPlatform().getPreference(PREF_NAME_THEME)
        .putString(PREF_KEY_COLOR_SCHEME_NAME, schemeName)
}

fun getColorPreference(): ColorPreference {
    return getPlatform().getPreference(PREF_NAME_THEME)
        .getObject(PREF_KEY_COLOR_PREFERENCE, ColorPreference.FollowSystem)
}

fun saveColorPreference(colorPreference: ColorPreference) {
    getPlatform().getPreference(PREF_NAME_THEME)
        .putObject(PREF_KEY_COLOR_PREFERENCE, colorPreference)
}
