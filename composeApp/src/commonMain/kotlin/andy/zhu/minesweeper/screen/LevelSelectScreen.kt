@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent
import andy.zhu.minesweeper.settings.loadGame
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val pageSize: DpSize = DpSize(260.dp, 160.dp)
private val pagePadding = 8.dp

@Composable
fun LevelSelectScreen(component: LevelSelectScreenComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var mainAreaSize: IntSize? by remember { mutableStateOf(null) }
        val dpSize = with(LocalDensity.current) {
            mainAreaSize?.toSize()?.toDpSize()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { mainAreaSize = it.size },
        ) {
            val coroutineScope = rememberCoroutineScope()

            if (dpSize != null && dpSize.height >= 500.dp) {
                val mineMapWidth = kotlin.math.ceil(dpSize.width / MineDrawConfig.defaultMineSize).toInt()
                val mineMapHeight = (dpSize.height * 0.4f / MineDrawConfig.defaultMineSize).toInt()
                PreviewMineMap(mineMapWidth, mineMapHeight, coroutineScope)
                Spacer(Modifier.height(32.dp))
            } else {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
            }

            Spacer(Modifier.height(16.dp))

            val savedGames = remember { mutableMapOf<GameConfig.Level, GameSave?>() }
            var showResumeButton by remember { mutableStateOf(false) }

            val widthDp = dpSize?.width ?: 360.dp
            val fullWidthPager = widthDp <= 420.dp
            val pagerWidth = if (fullWidthPager) widthDp else pageSize.width + pagePadding * 2
            val pagerState = rememberPagerState(pageCount = {
                gameConfigs.size
            })
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    val currentConfig = gameConfigs[it]
                    if (!savedGames.containsKey(currentConfig.level)) {
                        savedGames[currentConfig.level] = loadGame(currentConfig.level)
                    }
                    showResumeButton = savedGames[currentConfig.level] != null
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(pageSize.height),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!fullWidthPager) {
                    PagerBackwardButton(coroutineScope, pagerState)
                }
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(
                        start = (pagerWidth - pageSize.width) / 2,
                        end = (pagerWidth - pageSize.width) / 2 - pagePadding
                    ),
                    modifier = Modifier.width(pagerWidth),
                    pageSize = PageSize.Fixed(pageSize.width),
                    pageSpacing = pagePadding,
                ) {
                    LevelCard(gameConfigs[it])
                }
                if (!fullWidthPager) {
                    PagerForwardButton(coroutineScope, pagerState)
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    component.onLevelSelected(gameConfigs[pagerState.currentPage])
                },
                modifier = Modifier.width(pageSize.width),
            ) {
                Text("New Game")
            }

            AnimatedVisibility(
                showResumeButton,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                OutlinedButton(
                    onClick = {
                        savedGames[gameConfigs[pagerState.currentPage].level]?.let {
                            component.onGameResume(it)
                        }
                    },
                    modifier = Modifier.width(pageSize.width),
                ) {
                    Text("Resume")
                }
            }

            Spacer(Modifier.weight(1f))

            BottomMenu(pagerState.currentPage, component.navigation)
        }

        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}

@Composable
private fun LevelCard(gameConfig: GameConfig) {
    OutlinedCard(
        modifier = Modifier.size(pageSize),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Text(
                    gameConfig.name(),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                )
            }
            Box(
                modifier = Modifier.weight(1f).padding(start = 16.dp),
            ) {
                Text(
                    gameConfig.shortDesc,
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(
                modifier = Modifier.height(35.dp).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "${gameConfig.width}x${gameConfig.height}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painterResource("mine.xml"),
                    contentDescription = null,
                )
                Text(
                    gameConfig.mineCount.toString(),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun BottomMenu(currentPage: Int, navigation: LevelSelectScreenComponent.Navigation) {
    Row(Modifier.width(320.dp).padding(bottom = 16.dp)) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = navigation::settings) {
                Icon(Icons.Default.Settings, contentDescription = null)
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = navigation::about) {
                Icon(Icons.Default.Info, contentDescription = null)
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = navigation::palette) {
                Icon(painterResource("palette.xml"), contentDescription = null)
            }
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = { navigation.rank(gameConfigs[currentPage].level) }) {
                Icon(painterResource("list_numbered.xml"), contentDescription = null)
            }
        }
    }
}
