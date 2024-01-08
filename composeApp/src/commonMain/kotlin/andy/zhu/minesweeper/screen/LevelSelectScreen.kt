@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.GameConfig
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val levels = listOf(GameConfig.Easy, GameConfig.Medium, GameConfig.Hard)

@Composable
fun LevelSelectScreen(component: LevelSelectScreenComponent) {
    Scaffold(
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))

            Spacer(Modifier.height(64.dp))

            Icon(
                painterResource("mine.xml"),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(24.dp))

            val pagerState = rememberPagerState( pageCount = {
                levels.size
            })
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(start = 8.dp),
                modifier = Modifier.width(256.dp),
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

            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}

@Composable
private fun LevelCard(level: GameConfig) {
    Card(
        modifier = Modifier.width(240.dp).height(160.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            Text(
                level.levelName,
                modifier = Modifier.align(Alignment.TopStart),
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                "${level.width}x${level.height}",
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                Icon(
                    painterResource("mine.xml"),
                    contentDescription = null,
                )
                Text(
                    level.mineCount.toString(),
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}