@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameInstance
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.game.GameSave.Companion.saveKey
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent
import andy.zhu.minesweeper.settings.getObjectOrNull
import getPlatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val levels = listOf(GameConfig.Easy, GameConfig.Medium, GameConfig.Hard, GameConfig.Extreme)

private val pageSize: DpSize = DpSize(260.dp, 160.dp)
private val pagePadding = 8.dp

@Composable
fun LevelSelectScreen(component: LevelSelectScreenComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var mainAreaSize: IntSize by remember { mutableStateOf(IntSize(1080, 1920)) }
        val dpSize = with(LocalDensity.current) {
            mainAreaSize.toSize().toDpSize()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { mainAreaSize = it.size },
        ) {
            val coroutineScope = rememberCoroutineScope()

            if (dpSize.height >= 500.dp) {
                val gameInstance = remember(mainAreaSize) {
                    val mineMapWidth = kotlin.math.ceil(dpSize.width / MineDrawConfig.defaultMineSize).toInt()
                    val mineMapHeight = (dpSize.height * 0.4f / MineDrawConfig.defaultMineSize).toInt()
                    val mineCount = mineMapHeight * mineMapWidth / 8
                    GameInstance(GameConfig.Custom(mineMapWidth, mineMapHeight, mineCount), coroutineScope).apply {
                        onMineTap(Position(mineMapWidth / 2, mineMapHeight / 2))
                        onPause()
                    }
                }
                val drawConfig = CreateDrawConfig()
                Box(
                    modifier = Modifier.fillMaxWidth().height(MineDrawConfig.defaultMineSize * gameInstance.gameConfig.height)
                ) {
                    Canvas(
                        modifier = Modifier.fillMaxWidth().height(MineDrawConfig.defaultMineSize * gameInstance.gameConfig.height)
                    ) {
                        drawMines(Matrix(), gameInstance.mapUIFlow.value, 0f, drawConfig)
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(
                            Color.Transparent, Color.Transparent, Color.Transparent, MaterialTheme.colorScheme.surface
                        )))
                    )
                }
                Spacer(Modifier.height(32.dp))
            } else {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
            }

            Spacer(Modifier.height(16.dp))

            val savedGames = remember { mutableMapOf<GameConfig.Level, GameSave?>() }
            var showResumeButton by remember { mutableStateOf(false) }

            val fullWidthPager = dpSize.width <= 420.dp
            val pagerWidth = if (fullWidthPager) dpSize.width else pageSize.width + pagePadding * 2
            val pagerState = rememberPagerState(pageCount = {
                levels.size
            })
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    val currentConfig = levels[it]
                    if (!savedGames.containsKey(currentConfig.level)) {
                        savedGames[currentConfig.level] = getPlatform().settings.getObjectOrNull(currentConfig.saveKey())
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
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.canScrollBackward) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        },
                        enabled = pagerState.canScrollBackward,
                    ) {
                        Icon(painterResource("arrow_back.xml"), contentDescription = null)
                    }
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
                    LevelCard(levels[it])
                }
                if (!fullWidthPager) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.canScrollForward) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        enabled = pagerState.canScrollForward,
                    ) {
                        Icon(painterResource("arrow_forward.xml"), contentDescription = null)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    component.onLevelSelected(levels[pagerState.currentPage])
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
                        savedGames[levels[pagerState.currentPage].level]?.let {
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
            IconButton(onClick = { navigation.rank(levels[currentPage].level) }) {
                Icon(painterResource("list_numbered.xml"), contentDescription = null)
            }
        }
    }
}
