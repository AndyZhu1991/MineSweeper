package andy.zhu.minesweeper.settings

import andy.zhu.minesweeper.Database
import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.navigation.SettingsScreenComponent
import andy.zhu.minesweeper.theme.color.ColorConfig
import andy.zhu.minesweeper.theme.color.ColorPreference
import getPlatform
import org.koin.mp.KoinPlatform.getKoin

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

fun saveGame(level: GameConfig.Level, gameSave: GameSave) {
    removeGameSave(level)
    getKoin().get<Database>().run {
        gameSaveQueries.insert(
            level.name,
            gameSave.gameConfig.width.toLong(),
            gameSave.gameConfig.height.toLong(),
            gameSave.gameConfig.mineCount.toLong(),
            gameSave.timeMillis.toLong(),
            gameSave.mineIndicesStr(),
            gameSave.openedIndicesStr(),
            gameSave.flaggedIndicesStr(),
        )
    }
}

fun loadGameSave(level: GameConfig.Level): GameSave? {
    return getKoin().get<Database>().run {
        val gameSave = gameSaveQueries.get(level.name).executeAsList().getOrNull(0) ?: return@run null
        val gameConfig = when (level) {
            GameConfig.Level.Easy -> GameConfig.Easy
            GameConfig.Level.Medium -> GameConfig.Medium
            GameConfig.Level.Hard -> GameConfig.Hard
            GameConfig.Level.Extreme -> GameConfig.Extreme
            GameConfig.Level.Custom -> GameConfig.Custom(
                gameSave.width.toInt(),
                gameSave.height.toInt(),
                gameSave.mine_count.toInt(),
            )
        }
        GameSave(
            gameConfig,
            gameSave.mine_indices.split(",").mapNotNull { it.toIntOrNull() },
            gameSave.opened_indices.split(",").mapNotNull { it.toIntOrNull() },
            gameSave.flagged_indices.split(",").mapNotNull { it.toIntOrNull() },
            gameSave.time.toInt(),
        )
    }
}

fun removeGameSave(level: GameConfig.Level) {
    getKoin().get<Database>().run {
        gameSaveQueries.delete(level.name)
    }
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
