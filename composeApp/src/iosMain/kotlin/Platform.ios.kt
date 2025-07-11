import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import andy.zhu.minesweeper.Database
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.UIKit.UIDevice

object IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val isMobile: Boolean = true

    override fun getPreference(name: String?): Settings {
        return NSUserDefaultsSettings.Factory().create(name)
    }
}

actual fun getPlatform(): Platform = IOSPlatform

actual fun Modifier.mousePointerMatcher(
    button: MousePointerButton,
    onClick: (Offset) -> Unit
): Modifier = this

actual fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier = this

actual fun createSqlDriver(): SqlDriver {
    return NativeSqliteDriver(Database.Schema, "database.db")
}