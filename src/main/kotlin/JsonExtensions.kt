import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> Json.copy(value: T): T {
    return Json.decodeFromString(Json.encodeToString(value))
}