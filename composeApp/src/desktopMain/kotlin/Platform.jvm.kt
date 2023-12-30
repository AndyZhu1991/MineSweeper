import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.*

import androidx.compose.ui.input.pointer.onPointerEvent as jvmOnPointerEvent

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@OptIn(ExperimentalFoundationApi::class)
actual fun Modifier.mousePointerMatcher(
    button: MousePointerButton,
    onClick: (Offset) -> Unit
): Modifier {
    return pointerInput(Unit) {
        detectTapGestures(
            matcher = PointerMatcher.mouse(button.toPointerButton()),
            onTap = onClick
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier {
    return jvmOnPointerEvent(eventType, pass, onEvent)
}

private fun MousePointerButton.toPointerButton(): PointerButton {
    return when(this) {
        MousePointerButton.Primary -> PointerButton.Primary
        MousePointerButton.Secondary -> PointerButton.Secondary
        MousePointerButton.Tertiary -> PointerButton.Tertiary
        MousePointerButton.Back -> PointerButton.Back
        MousePointerButton.Forward -> PointerButton.Forward
    }
}