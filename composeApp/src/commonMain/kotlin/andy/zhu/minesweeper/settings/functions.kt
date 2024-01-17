package andy.zhu.minesweeper.settings

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.navigation.SettingsScreenComponent
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


/**************************************************************************************************/
// Settings
/**************************************************************************************************/

private const val PREF_NAME_SETTINGS = "settings"
private const val PREF_KEY_SHOW_ACTION_TOGGLE = "show_action_toggle"
private const val PREF_KEY_DEFAULT_ACTION = "default_action"

fun getShowActionToggle(): Boolean {
    return getPlatform().getPreference(PREF_NAME_SETTINGS)
        .getBoolean(PREF_KEY_SHOW_ACTION_TOGGLE, getPlatform().isMobile)
}

fun saveShowActionToggle(show: Boolean) {
    getPlatform().getPreference(PREF_NAME_SETTINGS)
        .putBoolean(PREF_KEY_SHOW_ACTION_TOGGLE, show)
}

fun getDefaultAction(): SettingsScreenComponent.DefaultAction {
    return getPlatform().getPreference(PREF_NAME_SETTINGS)
        .getObject(PREF_KEY_DEFAULT_ACTION, SettingsScreenComponent.DefaultAction.Flag)
}

fun saveDefaultAction(defaultAction: SettingsScreenComponent.DefaultAction) {
    getPlatform().getPreference(PREF_NAME_SETTINGS)
        .putObject(PREF_KEY_DEFAULT_ACTION, defaultAction)
}


/**************************************************************************************************/
// Custom game
/**************************************************************************************************/

private const val PREF_NAME_CUSTOM_GAME = "custom_game"
private const val PREF_KEY_WIDTH = "width"
private const val PREF_KEY_HEIGHT = "height"
private const val PREF_KEY_MINE_COUNT = "mine_count"

fun saveCustomGame(width: Int, height: Int, mineCount: Int) {
    getPlatform().getPreference(PREF_NAME_CUSTOM_GAME).run {
        putInt(PREF_KEY_WIDTH, width)
        putInt(PREF_KEY_HEIGHT, height)
        putInt(PREF_KEY_MINE_COUNT, mineCount)
    }
}

fun getCustomGame(): GameConfig? {
    return getPlatform().getPreference(PREF_NAME_CUSTOM_GAME).run {
        val width = getInt(PREF_KEY_WIDTH, -1)
        val height = getInt(PREF_KEY_HEIGHT, -1)
        val mineCount = getInt(PREF_KEY_MINE_COUNT, -1)
        if (width > 0 && height > 0 && mineCount > 0) {
            GameConfig.Custom(width, height, mineCount)
        } else {
            null
        }
    }
}
