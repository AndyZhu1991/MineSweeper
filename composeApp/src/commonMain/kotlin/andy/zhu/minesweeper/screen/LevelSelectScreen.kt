@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.GameConfig
import andy.zhu.minesweeper.GameInstance
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val levels = listOf(GameConfig.Easy, GameConfig.Medium, GameConfig.Hard)

@Composable
fun LevelSelectScreen(component: LevelSelectScreenComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
//        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))

        var mainAreaSize: IntSize? by remember { mutableStateOf(null) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { mainAreaSize = it.size },
        ) {
            with(LocalDensity.current) {
                mainAreaSize?.toSize()?.toDpSize()?.let { dpSize ->
                    val coroutineScope = rememberCoroutineScope()
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
                    Canvas(
                        modifier = Modifier.fillMaxWidth().height(MineDrawConfig.defaultMineSize * gameInstance.gameConfig.height)
                    ) {
                        drawMines(Matrix(), gameInstance.mapUIFlow.value, 0f, drawConfig)
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            val pagerState = rememberPagerState(pageCount = {
                levels.size
            })
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(start = 20.dp, end = 12.dp),
                modifier = Modifier.width(280.dp),
                pageSize = PageSize.Fixed(240.dp),
                pageSpacing = 8.dp,
            ) {
                LevelCard(levels[it])
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    component.onLevelSelected(levels[pagerState.currentPage])
                },
                modifier = Modifier.width(240.dp),
            ) {
                Text("Start")
            }
        }

        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}

@Composable
private fun LevelCard(level: GameConfig) {
    OutlinedCard(
        modifier = Modifier.width(240.dp).height(160.dp),
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
                    level.levelName,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.displayMedium,
                    )
            }
            Box(
                modifier = Modifier.weight(1f).padding(start = 16.dp),
            ) {
                Text(
                    "This level is for beginners.",
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(
                modifier = Modifier.height(35.dp).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "${level.width}x${level.height}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painterResource("mine.xml"),
                    contentDescription = null,
                )
                Text(
                    level.mineCount.toString(),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
//                Row(
//                    modifier = Modifier.align(Alignment.BottomEnd),
//                ) {
//                }
            }
        }
    }
}