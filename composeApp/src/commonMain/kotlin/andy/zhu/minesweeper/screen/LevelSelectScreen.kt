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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.game.GameConfig
import andy.zhu.minesweeper.game.GameSave
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent
import andy.zhu.minesweeper.settings.getCustomGame
import andy.zhu.minesweeper.settings.loadGame
import andy.zhu.minesweeper.settings.saveCustomGame
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private val pageSize: DpSize = DpSize(270.dp, 170.dp)
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

            var showCustomDialog by remember { mutableStateOf(false) }

            var customGame by remember { mutableStateOf(
                getCustomGame() ?: GameConfig.Custom(10, 10, 1)
            ) }
            if (showCustomDialog) {
                CustomGameDialog(
                    customGame,
                    onDismiss = { showCustomDialog = false }
                ) {
                    customGame = it
                    showCustomDialog = false
                    saveCustomGame(it.width, it.height, it.mineCount)
                }
            }

            val gameConfigs = gameConfigsWithOutCustom + listOf(customGame)

            val savedGames = remember { mutableMapOf<GameConfig.Level, GameSave?>() }
            var showResumeButton by remember { mutableStateOf(false) }

            val widthDp = dpSize?.width ?: 360.dp
            val fullWidthPager = widthDp <= 420.dp
            val pagerWidth = if (fullWidthPager) widthDp else pageSize.width + pagePadding * 2
            val pagerState = rememberPagerState(pageCount = {
                gameConfigs.size
            })
            LaunchedEffect(pagerState, customGame) {
                snapshotFlow { pagerState.currentPage }.collect {
                    val currentConfig = gameConfigs[it]
                    if (!savedGames.containsKey(currentConfig.level)) {
                        savedGames[currentConfig.level] = loadGame(currentConfig.level)
                    }
                    val gameSave = savedGames[currentConfig.level]
                    showResumeButton = if (gameSave?.gameConfig?.level == GameConfig.Level.Custom) {
                        gameSave.gameConfig == customGame
                    } else {
                        gameSave != null
                    }
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
                    LevelCard(gameConfigs[it]) { showCustomDialog = true }
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
                        val gameSave = savedGames[gameConfigs[pagerState.currentPage].level] ?: return@OutlinedButton
                        if (gameSave.gameConfig.level == GameConfig.Level.Custom) {
                            if (gameSave.gameConfig != customGame) {
                                return@OutlinedButton
                            }
                        }
                        component.onGameResume(gameSave)
                    },
                    modifier = Modifier.width(pageSize.width),
                ) {
                    Text("Resume")
                }
            }

            Spacer(Modifier.weight(1f))

            BottomMenu(gameConfigs[pagerState.currentPage].level, component.navigation)
        }

        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}

@Composable
private fun LevelCard(gameConfig: GameConfig, onCustom: () -> Unit) {
    OutlinedCard(
        modifier = Modifier.size(pageSize),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            ) {
                Text(
                    gameConfig.shortDesc,
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.titleMedium,
                )
                if (gameConfig.level == GameConfig.Level.Custom) {
                    IconButton(
                        onClick = onCustom,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(
                            painterResource("tune.xml"),
                            contentDescription = null,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.height(38.dp).padding(horizontal = 16.dp),
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
fun BottomMenu(currentLevel: GameConfig.Level, navigation: LevelSelectScreenComponent.Navigation) {
    var showAboutDialog by remember { mutableStateOf(false) }
    if (showAboutDialog) {
        AboutDialog { showAboutDialog = false }
    }

    Row(Modifier.width(320.dp).padding(bottom = 16.dp)) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = navigation::settings) {
                Icon(Icons.Default.Settings, contentDescription = null)
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = { showAboutDialog = true }) {
                Icon(Icons.Default.Info, contentDescription = null)
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = navigation::palette) {
                Icon(painterResource("palette.xml"), contentDescription = null)
            }
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = { navigation.rank(currentLevel) }) {
                Icon(painterResource("list_numbered.xml"), contentDescription = null)
            }
        }
    }
}

private const val MIN_MAP_SIZE = 3
private const val MAX_MAP_SIZE = 100

@Composable
fun CustomGameDialog(
    gameConfig: GameConfig,
    onDismiss: () -> Unit,
    onConfirm: (GameConfig) -> Unit,
) {
    var width by remember { mutableStateOf(gameConfig.width.toString()) }
    var height by remember { mutableStateOf(gameConfig.height.toString()) }
    var mineCount by remember { mutableStateOf(gameConfig.mineCount.toString()) }

    val isWidthError = width.toIntOrNull() !in MIN_MAP_SIZE..MAX_MAP_SIZE
    val isHeightError = height.toIntOrNull() !in MIN_MAP_SIZE..MAX_MAP_SIZE
    val isMineCountError = if (isWidthError || isHeightError) {
        false
    } else {
        val intCount = mineCount.toIntOrNull()
        if (intCount == null) {
            true
        } else {
            intCount !in 1..(width.toInt() * height.toInt() / 2)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Custom Game") },
        text = {
            Column {
                OutlinedTextField(
                    value = width,
                    label = { Text("Width") },
                    singleLine = true,
                    isError = isWidthError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        if (it.all(Char::isDigit) && it.length <= 4) {
                            width = it
                        }
                    },
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = height,
                    label = { Text("Height") },
                    singleLine = true,
                    isError = isHeightError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        if (it.all(Char::isDigit) && it.length <= 4) {
                            height = it
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = mineCount,
                    label = { Text("Mine count") },
                    singleLine = true,
                    isError = isMineCountError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        if (it.all(Char::isDigit) && it.length <= 4) {
                            mineCount = it
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = !isWidthError && !isHeightError && !isMineCountError,
                onClick = {
                    onConfirm(
                        GameConfig.Custom(width.toInt(), height.toInt(), mineCount.toInt())
                    )
                    onDismiss()
                },
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {},
        text = { AboutContent() },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("OK")
            }
        },
    )
}
