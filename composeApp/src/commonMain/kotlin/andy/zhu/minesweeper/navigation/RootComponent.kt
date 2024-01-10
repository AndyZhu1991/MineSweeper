package andy.zhu.minesweeper.navigation

import andy.zhu.minesweeper.game.GameConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    private val navigation = StackNavigation<Config>()
    val childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.LevelSelectScreen,
        handleBackButton = true,
        childFactory = ::createChild,
    )
    
    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): Child {
        return when(config) {
            Config.LevelSelectScreen -> {
                Child.LevelSelectScreen(LevelSelectScreenComponent(
                    componentContext = context,
                    startGame = { level ->
                        navigation.push(Config.MainGameScreen(level))
                    }
                ))
            }
            is Config.MainGameScreen -> {
                Child.MainGameScreen(MainGameScreenComponent(
                    context,
                    config.level,
                    navigation::pop,
                ))
            }
        }
    }
    
    sealed class Child {
        data class LevelSelectScreen(val component: LevelSelectScreenComponent): Child()
        data class MainGameScreen(val component: MainGameScreenComponent): Child()
    }
    
    @Serializable
    sealed class Config {
        @Serializable
        data object LevelSelectScreen : Config()
        
        @Serializable
        data class MainGameScreen(val level: GameConfig) : Config()
    }
}