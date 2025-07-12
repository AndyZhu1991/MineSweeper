@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalResourceApi::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.navigation.RankScreenComponent
import andy.zhu.minesweeper.settings.getRank
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun RankScreen(component: RankScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Rank",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val coroutineScope = rememberCoroutineScope()
            val initialPage = gameConfigsWithOutCustom
                .indexOfFirst { it.level == component.currentLevel }
                .takeIf { it >= 0 } ?: 0
            val pagerState = rememberPagerState(
                initialPage = initialPage,
                pageCount = { gameConfigsWithOutCustom.size },
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                PagerBackwardButton(coroutineScope, pagerState)
                Text(
                    gameConfigsWithOutCustom[pagerState.currentPage].level.name,
                    modifier = Modifier.width(200.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
                PagerForwardButton(coroutineScope, pagerState)
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .width(280.dp)
                    .padding(top = 16.dp),
            ) { page ->
                val rank = getRank(gameConfigsWithOutCustom[page])
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    rank.forEachIndexed { index, rankItem ->
                        RankLine(
                            index + 1,
                            (rankItem.time_millis / 1000).toInt(),
                            rankItem.created_at,
                            false
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}