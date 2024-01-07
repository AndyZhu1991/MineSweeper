package andy.zhu.minesweeper.settings

import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> Settings.putObject(key: String, value: T) {
    putString(key, Json.encodeToString(value))
}

inline fun <reified T> Settings.getObject(key: String, defaultValue: T): T {
    return try {
        getStringOrNull(key)?.let {
            Json.decodeFromString<T>(it)
        }
    } catch (e: Exception) {
        null
    } ?: defaultValue
}

inline fun <reified T> Settings.getObjectOrNull(key: String): T? {
    return try {
        getStringOrNull(key)?.let {
            Json.decodeFromString<T>(it)
        }
    } catch (e: Exception) {
        null
    }
}