@file:OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package andy.zhu.minesweeper.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.drawMines
import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal val gameConfigsWithOutCustom = listOf(GameConfig.Easy, GameConfig.Medium, GameConfig.Hard, GameConfig.Extreme)

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
        Box(modifier = Modifier.size(24.dp)) {
            Icon(
                painterResource("numeric_$order.png"),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
            Box(
                modifier = Modifier.fillMaxSize().padding(end = 1.dp, bottom = 4.5.dp)
            ) {
                Box(Modifier
                    .size(2.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
        Text(
            Instant.fromEpochMilliseconds(timeStamp).toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
            modifier = Modifier.padding(start = 4.dp, bottom = 0.5.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "${score}s",
            modifier = Modifier.padding(bottom = 0.5.dp, end = 3.dp),
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

@Composable
internal fun PreviewMineMap(
    mineMapWidth: Int,
    mineMapHeight: Int,
    coroutineScope: CoroutineScope,
) {
    val gameInstance = remember(mineMapWidth, mineMapHeight) {
        val mineCount = mineMapHeight * mineMapWidth / 8
        GameInstance(GameConfig.Custom(mineMapWidth, mineMapHeight, mineCount), coroutineScope).apply {
            onMineTap(Position(mineMapWidth / 2, mineMapHeight / 2))
            flagAllCanBeFlagged()
            onPause()
            refreshUi()
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
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(
                Color.Transparent, Color.Transparent, Color.Transparent, MaterialTheme.colorScheme.surface
            )))
        )
    }
}
