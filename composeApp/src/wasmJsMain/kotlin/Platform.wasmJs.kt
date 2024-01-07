import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

object WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"

    override val isMobile: Boolean = false

    override val settings: Settings = StorageSettings()
}

actual fun getPlatform(): Platform = WasmPlatform

actual fun Modifier.mousePointerMatcher(
    button: MousePointerButton,
    onClick: (Offset) -> Unit
): Modifier = this

actual fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier = this