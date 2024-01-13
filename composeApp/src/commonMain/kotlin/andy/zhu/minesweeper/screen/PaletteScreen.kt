package andy.zhu.minesweeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.navigation.PaletteScreenComponent
import andy.zhu.minesweeper.settings.getColorSchemeName
import andy.zhu.minesweeper.settings.getFollowSystem
import andy.zhu.minesweeper.settings.getPreferLight
import andy.zhu.minesweeper.theme.color.ColorConfig

@Composable
fun PaletteScreen(component: PaletteScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Palette",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
        var mainAreaSize: IntSize? by remember { mutableStateOf(null) }
        val dpSize = with(LocalDensity.current) {
            mainAreaSize?.toSize()?.toDpSize()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onGloballyPositioned { mainAreaSize = it.size },
        ) {
            if (dpSize != null && dpSize.height >= 500.dp) {
                val mineMapWidth = kotlin.math.ceil(dpSize.width / MineDrawConfig.defaultMineSize).toInt()
                val mineMapHeight = (dpSize.height * 0.4f / MineDrawConfig.defaultMineSize).toInt()
                PreviewMineMap(mineMapWidth, mineMapHeight, component.coroutineScope)
                Spacer(Modifier.height(32.dp))
            }

            var selectedColor by remember { mutableStateOf(getColorSchemeName()) }
            val isSystemDark = isSystemInDarkTheme()
            val followSystem = getFollowSystem()
            val preferLight = getPreferLight()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                ColorConfig.colorNames.forEach { colorName ->
                    val isSelected = colorName == selectedColor
                    ColorSchemeSelectionItem(
                        colorConfig = ColorConfig(
                            schemeName = colorName,
                            isSystemDark = isSystemDark,
                            followSystem = followSystem,
                            preferLight = preferLight,
                        ),
                        isSelected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                selectedColor = colorName
                                component.onColorSchemeSelected(colorName)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSchemeSelectionItem(colorConfig: ColorConfig, isSelected: Boolean, onClick: () -> Unit) {
    val colorScheme = colorConfig.resolveColorScheme()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.background(colorScheme.primaryContainer)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            colorConfig.schemeName,
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.weight(1f))
        Box(modifier = Modifier
            .size(48.dp)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.primary, shape = CircleShape),
        )
        Box(modifier = Modifier
            .size(48.dp)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.secondary, shape = CircleShape),
        )
        Box(modifier = Modifier
            .size(48.dp)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.tertiary, shape = CircleShape),
        )
        Spacer(Modifier.width(8.dp))
    }
}