import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun PreviewLightColors() {
    PreviewColors(lightColorScheme())
}

@Preview
@Composable
fun PreviewDarkColors() {
    PreviewColors(darkColorScheme())
}

@Composable
fun PreviewColors(colorScheme: ColorScheme) {
    Column {
        PreviewColor("primary", colorScheme.primary)
        PreviewColor("onPrimary", colorScheme.onPrimary)
        PreviewColor("primaryContainer", colorScheme.primaryContainer)
        PreviewColor("onPrimaryContainer", colorScheme.onPrimaryContainer)
        PreviewColor("inversePrimary", colorScheme.inversePrimary)
        PreviewColor("secondary", colorScheme.secondary)
        PreviewColor("onSecondary", colorScheme.onSecondary)
        PreviewColor("secondaryContainer", colorScheme.secondaryContainer)
        PreviewColor("onSecondaryContainer", colorScheme.onSecondaryContainer)
        PreviewColor("tertiary", colorScheme.tertiary)
        PreviewColor("onTertiary", colorScheme.onTertiary)
        PreviewColor("tertiaryContainer", colorScheme.tertiaryContainer)
        PreviewColor("onTertiaryContainer", colorScheme.onTertiaryContainer)
        PreviewColor("background", colorScheme.background)
        PreviewColor("onBackground", colorScheme.onBackground)
        PreviewColor("surface", colorScheme.surface)
        PreviewColor("onSurface", colorScheme.onSurface)
        PreviewColor("surfaceVariant", colorScheme.surfaceVariant)
        PreviewColor("onSurfaceVariant", colorScheme.onSurfaceVariant)
        PreviewColor("surfaceTint", colorScheme.surfaceTint)
        PreviewColor("inverseSurface", colorScheme.inverseSurface)
        PreviewColor("inverseOnSurface", colorScheme.inverseOnSurface)
        PreviewColor("error", colorScheme.error)
        PreviewColor("onError", colorScheme.onError)
        PreviewColor("errorContainer", colorScheme.errorContainer)
        PreviewColor("onErrorContainer", colorScheme.onErrorContainer)
        PreviewColor("outline", colorScheme.outline)
        PreviewColor("outlineVariant", colorScheme.outlineVariant)
        PreviewColor("scrim", colorScheme.scrim)
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