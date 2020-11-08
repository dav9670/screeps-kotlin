package messages

import kotlinx.serialization.json.Json
import misc.extensions.copy

abstract class Message<S : Sender, in R : Receiver> {
    abstract val sender: S
    abstract val priority: Priority

    abstract fun affinity(receiver: R): Double

    fun copy(): Message<S, R> {
        return Json.copy(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as Message<*, *>

        if (sender != other.sender) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sender.hashCode()
        result = 31 * result + priority.hashCode()
        return result
    }

    override fun toString(): String {
        return "Message(sender=$sender, priority=$priority)"
    }

    enum class Priority {
        Critical,
        High,
        Medium,
        Low,
        None
    }
}