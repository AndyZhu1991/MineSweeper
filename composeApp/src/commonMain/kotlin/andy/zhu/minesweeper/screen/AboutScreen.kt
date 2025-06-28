package andy.zhu.minesweeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import andy.zhu.minesweeper.navigation.AboutScreenComponent
import minesweeper.composeapp.generated.resources.Res
import minesweeper.composeapp.generated.resources.mine
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun AboutScreen(component: AboutScreenComponent) {
    Scaffold(
        topBar = { SimpleTopAppBar("About",0.5f, onBack = component.onClose) },
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            AboutContent()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboutContent() {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.mine),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    "Minesweeper",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    "Material designed",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Text(
            "Powered by Compose Multiplatform",
            modifier = Modifier.padding(top = 40.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}