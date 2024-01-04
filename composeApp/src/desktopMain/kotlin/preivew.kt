import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Preview all colors in MaterialTheme.colorScheme.
 */
@Preview
@Composable
fun PreviewColors() {
    Column {
        PreviewColor("primary", MaterialTheme.colorScheme.primary)
        PreviewColor("onPrimary", MaterialTheme.colorScheme.onPrimary)
        PreviewColor("primaryContainer", MaterialTheme.colorScheme.primaryContainer)
        PreviewColor("onPrimaryContainer", MaterialTheme.colorScheme.onPrimaryContainer)
        PreviewColor("inversePrimary", MaterialTheme.colorScheme.inversePrimary)
        PreviewColor("secondary", MaterialTheme.colorScheme.secondary)
        PreviewColor("onSecondary", MaterialTheme.colorScheme.onSecondary)
        PreviewColor("secondaryContainer", MaterialTheme.colorScheme.secondaryContainer)
        PreviewColor("onSecondaryContainer", MaterialTheme.colorScheme.onSecondaryContainer)
        PreviewColor("tertiary", MaterialTheme.colorScheme.tertiary)
        PreviewColor("onTertiary", MaterialTheme.colorScheme.onTertiary)
        PreviewColor("tertiaryContainer", MaterialTheme.colorScheme.tertiaryContainer)
        PreviewColor("onTertiaryContainer", MaterialTheme.colorScheme.onTertiaryContainer)
        PreviewColor("background", MaterialTheme.colorScheme.background)
        PreviewColor("onBackground", MaterialTheme.colorScheme.onBackground)
        PreviewColor("surface", MaterialTheme.colorScheme.surface)
        PreviewColor("onSurface", MaterialTheme.colorScheme.onSurface)
        PreviewColor("surfaceVariant", MaterialTheme.colorScheme.surfaceVariant)
        PreviewColor("onSurfaceVariant", MaterialTheme.colorScheme.onSurfaceVariant)
        PreviewColor("surfaceTint", MaterialTheme.colorScheme.surfaceTint)
        PreviewColor("inverseSurface", MaterialTheme.colorScheme.inverseSurface)
        PreviewColor("inverseOnSurface", MaterialTheme.colorScheme.inverseOnSurface)
        PreviewColor("error", MaterialTheme.colorScheme.error)
        PreviewColor("onError", MaterialTheme.colorScheme.onError)
        PreviewColor("errorContainer", MaterialTheme.colorScheme.errorContainer)
        PreviewColor("onErrorContainer", MaterialTheme.colorScheme.onErrorContainer)
        PreviewColor("outline", MaterialTheme.colorScheme.outline)
        PreviewColor("outlineVariant", MaterialTheme.colorScheme.outlineVariant)
        PreviewColor("scrim", MaterialTheme.colorScheme.scrim)
    }
}

@Composable
private fun PreviewColor(name: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, modifier = Modifier.weight(1f))
        Surface(
            modifier = Modifier.height(32.dp).weight(2f),
            color = color
        ) {}
    }
}