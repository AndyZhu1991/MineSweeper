@file:OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.game.GameConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal val gameConfigs = listOf(GameConfig.Easy, GameConfig.Medium, GameConfig.Hard, GameConfig.Extreme)
internal val gameConfigsWithOutCustom = gameConfigs.filter { it.level != GameConfig.Level.Custom }

@Composable
internal fun RankLine(order: Int, score: Int, timeStamp: Long, isCurrent: Boolean) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .then(
                if (isCurrent)
                    Modifier.background(MaterialTheme.colorScheme.inversePrimary, RoundedCornerShape(2.dp))
                else
                    Modifier
            ),
    ) {
        Icon(
            painterResource("numeric_$order.png"),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Text(
            Instant.fromEpochMilliseconds(timeStamp).toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
            modifier = Modifier.padding(bottom = 1.5.dp),
        )
        Surface(
            modifier = Modifier.weight(1f)
        ) {}
        Text(
            "${score}s",
            modifier = Modifier.padding(bottom = 1.5.dp, end = 3.dp),
        )
    }
}

@Composable
internal fun SimpleTopAppBar(title: String, alpha: Float = 1f, onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
        ),
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
internal fun PagerBackwardButton(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
) {
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

@Composable
internal fun PagerForwardButton(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
) {
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
