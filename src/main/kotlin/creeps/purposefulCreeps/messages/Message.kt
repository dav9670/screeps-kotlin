package creeps.purposefulCreeps.messages

import copy
import kotlinx.serialization.json.Json
import screeps.api.Game
import screeps.api.Identifiable

abstract class Message<S : Identifiable, in R : Identifiable>(var repeat: Int = 1) {
    protected abstract val senderId: String
    abstract val priority: Priority

    val type = this::class.simpleName

    open val sender: S
        get() {
            return Game.getObjectById(senderId)!!
        }

    abstract fun affinity(receiver: R): Double

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