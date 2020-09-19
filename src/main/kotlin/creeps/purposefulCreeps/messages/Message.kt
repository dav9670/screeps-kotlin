package creeps.purposefulCreeps.messages

import copy
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import screeps.api.Game
import screeps.api.Identifiable
import screeps.utils.lazyPerTick

@Serializable
abstract class Message<S : Identifiable, in R : Identifiable>(var repeat: Int = 1) {
    protected abstract val senderId: String
    abstract val priority: Priority

    val type = this::class.simpleName

    open val sender by lazyPerTick<Message<S, R>, S> {
        Game.getObjectById(senderId)!!
    }

    companion object Factory {
        fun deserialize(content: String): Message<*, *> {
            val json = Json.decodeFromString<JsonObject>(content)

            when (val type = json["type"].toString()) {
                NeedCarryMessage::class.simpleName -> return Json.decodeFromString<NeedCarryMessage>(content)
                else -> throw Exception("Type $type is not recognised as message type")
            }
        }
    }

    abstract fun affinity(receiver: R): Double

    open fun serialize(): String {
        return Json.encodeToString(this)
    }

    fun copy(): Message<S, R> {
        return Json.copy(this)
    }

    enum class Priority {
        Critical,
        High,
        Medium,
        Low,
        None
    }
}