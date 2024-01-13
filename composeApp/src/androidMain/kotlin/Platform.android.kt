import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import andy.zhu.minesweeper.MineApplication
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

object AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val isMobile: Boolean = true

    override fun getPreference(name: String?): Settings {
        return SharedPreferencesSettings.Factory(MineApplication.instance).create(name)
    }
}

actual fun getPlatform(): Platform = AndroidPlatform

actual fun Modifier.mousePointerMatcher(
    button: MousePointerButton,
    onClick: (Offset) -> Unit
): Modifier = this

actual fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier = this