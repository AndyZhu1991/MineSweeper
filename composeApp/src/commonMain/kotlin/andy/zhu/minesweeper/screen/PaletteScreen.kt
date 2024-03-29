package andy.zhu.minesweeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import andy.zhu.minesweeper.MineDrawConfig
import andy.zhu.minesweeper.navigation.PaletteScreenComponent
import andy.zhu.minesweeper.settings.getColorPreference
import andy.zhu.minesweeper.settings.getColorSchemeName
import andy.zhu.minesweeper.theme.color.ColorConfig
import andy.zhu.minesweeper.theme.color.ColorPreference

@Composable
fun PaletteScreen(component: PaletteScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("Palette",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
        var mainAreaSize: IntSize? by remember { mutableStateOf(null) }
        val dpSize = with(LocalDensity.current) {
            mainAreaSize?.toSize()?.toDpSize()
        }
        val layoutDirection = LocalLayoutDirection.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(layoutDirection),
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(layoutDirection),
                )
                .onGloballyPositioned { mainAreaSize = it.size },
        ) {
            if (dpSize != null && dpSize.height >= 500.dp) {
                val mineMapWidth = kotlin.math.ceil(dpSize.width / MineDrawConfig.defaultMineSize).toInt()
                val mineMapHeight = (dpSize.height * 0.4f / MineDrawConfig.defaultMineSize).toInt()
                PreviewMineMap(mineMapWidth, mineMapHeight, component.coroutineScope)
                Spacer(Modifier.height(32.dp))
            }

            var colorPreference by remember { mutableStateOf(getColorPreference()) }
            ColorPreferenceSelectionItem(
                colorPreference = colorPreference,
                onColorPreferenceSelected = {
                    colorPreference = it
                    component.onColorPreferenceSelected(it)
                },
            )

            var selectedColor by remember { mutableStateOf(getColorSchemeName()) }
            val isSystemDark = isSystemInDarkTheme()
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
                            colorPreference,
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
                Spacer(Modifier.height(paddingValues.calculateBottomPadding()))
            }
        }
    }
}

private val itemHeight = 48.dp

@Composable
fun ColorSchemeSelectionItem(colorConfig: ColorConfig, isSelected: Boolean, onClick: () -> Unit) {
    val colorScheme = colorConfig.resolveColorScheme()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
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
            .size(itemHeight)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.primary, shape = CircleShape),
        )
        Box(modifier = Modifier
            .size(itemHeight)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.secondary, shape = CircleShape),
        )
        Box(modifier = Modifier
            .size(itemHeight)
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .background(colorScheme.tertiary, shape = CircleShape),
        )
        Spacer(Modifier.width(8.dp))
    }
}

@Composable
fun ColorPreferenceSelectionItem(
    colorPreference: ColorPreference,
    onColorPreferenceSelected: (ColorPreference) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight),
    ) {
        Text(
            "Appearance",
            modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterStart),
        )

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {

            RadioButton(
                selected = colorPreference == ColorPreference.FollowSystem,
                onClick = { onColorPreferenceSelected(ColorPreference.FollowSystem) },
                modifier = Modifier.offset(x = 32.dp),
            )
            Text(
                "System",
                modifier = Modifier.offset(x = 24.dp),
            )

            RadioButton(
                selected = colorPreference == ColorPreference.Light,
                onClick = { onColorPreferenceSelected(ColorPreference.Light) },
                modifier = Modifier.offset(x = 20.dp),
            )
            Text(
                "Light",
                modifier = Modifier.offset(x = 12.dp),
            )

            RadioButton(
                selected = colorPreference == ColorPreference.Dark,
                onClick = { onColorPreferenceSelected(ColorPreference.Dark) },
                modifier = Modifier.offset(x = 8.dp),
            )
            Text(
                "Dark",
                modifier = Modifier.padding(end = 16.dp),
            )
        }
    }
}