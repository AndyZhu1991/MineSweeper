import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import com.russhwolf.settings.Settings

interface Platform {
    val name: String
    val isMobile: Boolean
    val isLandscape: Boolean get() { return isMobile }
    fun getPreference(name: String? = null): Settings
}

expect fun getPlatform(): Platform

expect fun Modifier.mousePointerMatcher(
    button: MousePointerButton,
    onClick: (Offset) -> Unit
): Modifier

expect fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass = PointerEventPass.Main,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier

enum class MousePointerButton {
    Primary, Secondary, Tertiary, Back, Forward
}