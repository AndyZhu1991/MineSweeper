package andy.zhu.minesweeper.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import andy.zhu.minesweeper.GameLevel
import andy.zhu.minesweeper.navigation.LevelSelectScreenComponent

@Composable
fun LevelSelectScreen(component: LevelSelectScreenComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { component.onLevelSelected(GameLevel.Low) },
        ) {
            Text("Low")
        }
    }
}