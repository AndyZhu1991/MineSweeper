package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.settings.getColorPreference
import andy.zhu.minesweeper.settings.getColorSchemeName
import andy.zhu.minesweeper.settings.saveColorPreference
import andy.zhu.minesweeper.settings.saveColorSchemeName
import andy.zhu.minesweeper.theme.color.ColorPreference
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    val setStatusBarDark: (Boolean) -> Unit = {},
) : ComponentContext by componentContext {

    private val _colorSchemeName = MutableStateFlow(getColorSchemeName())
    val colorSchemeName: StateFlow<String> = _colorSchemeName

    private val _colorPreference = MutableStateFlow(getColorPreference())
    val colorPreference: StateFlow<ColorPreference> = _colorPreference
    
    private val navigation = StackNavigation<Config>()
    val childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.LevelSelectScreen,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun onColorSchemeChanged(schemeName: String) {
        _colorSchemeName.value = schemeName
        saveColorSchemeName(schemeName)
    }

    private fun onColorPreferenceChanged(colorPreference: ColorPreference) {
        _colorPreference.value = colorPreference
        saveColorPreference(colorPreference)
    }

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): Child {
        return when(config) {
            Config.LevelSelectScreen -> {
                Child.LevelSelectScreen(LevelSelectScreenComponent(
                    componentContext = context,
                    LevelSelectNavigation(),
                ))
            }
            is Config.MainGameScreen -> {
                Child.MainGameScreen(MainGameScreenComponent(
                    context,
                    config.level,
                    navigation::pop,
                ))
            }
            is Config.MainGameScreenFromSave -> {
                Child.MainGameScreen(MainGameScreenComponent(
                    context,
                    config.gameSave,
                    navigation::pop,
                ))
            }
            Config.AboutScreen -> {
                Child.AboutScreen(AboutScreenComponent(
                    componentContext = context,
                    navigation::pop,
                ))
            }
            Config.PaletteScreen -> {
                Child.PaletteScreen(PaletteScreenComponent(
                    componentContext = context,
                    navigation::pop,
                    ::onColorSchemeChanged,
                    ::onColorPreferenceChanged,
                ))
            }
            is Config.RankScreen -> {
                Child.RankScreen(RankScreenComponent(
                    componentContext = context,
                    currentLevel = config.currentLevel,
                    navigation::pop,
                ))
            }
            Config.SettingsScreen -> {
                Child.SettingsScreen(SettingsScreenComponent(
                    componentContext = context,
                    navigation::pop,
                ))
            }
        }
    }
    
    sealed class Child {
        data class LevelSelectScreen(val component: LevelSelectScreenComponent): Child()
        data class MainGameScreen(val component: MainGameScreenComponent): Child()
        data class AboutScreen(val component: AboutScreenComponent): Child()
        data class PaletteScreen(val component: PaletteScreenComponent): Child()
        data class RankScreen(val component: RankScreenComponent): Child()
        data class SettingsScreen(val component: SettingsScreenComponent): Child()
    }
    
    @Serializable
    sealed class Config {
        @Serializable
        data object LevelSelectScreen : Config()
        
        @Serializable
        data class MainGameScreen(val level: GameConfig) : Config()

        @Serializable
        data class MainGameScreenFromSave(val gameSave: GameSave): Config()

        @Serializable
        data object AboutScreen : Config()

        @Serializable
        data object PaletteScreen : Config()

        @Serializable
        data class RankScreen(val currentLevel: GameConfig.Level) : Config()

        @Serializable
        data object SettingsScreen : Config()
    }

    private inner class LevelSelectNavigation: LevelSelectScreenComponent.Navigation {
        override fun startGame(level: GameConfig) {
            navigation.push(Config.MainGameScreen(level))
        }

        override fun resumeGame(gameSave: GameSave) {
            navigation.push(Config.MainGameScreenFromSave(gameSave))
        }

        override fun settings() {
            navigation.push(Config.SettingsScreen)
        }

        override fun palette() {
            navigation.push(Config.PaletteScreen)
        }

        override fun rank(currentLevel: GameConfig.Level) {
            navigation.push(Config.RankScreen(currentLevel))
        }

        override fun about() {
            navigation.push(Config.AboutScreen)
        }
    }
}