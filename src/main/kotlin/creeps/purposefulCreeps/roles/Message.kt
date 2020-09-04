package creeps.purposefulCreeps.roles

import copy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import screeps.api.Identifiable

@Serializable
abstract class Message<S : Identifiable, in R : Identifiable>(val sender: S, val priority: Priority, var repeat: Int = 1) {
    enum class Priority {
        Critical,
        High,
        Medium,
        Low,
        None
    }

    abstract fun affinity(receiver: R): Double

    fun copy(): Message<S, R> {
        return Json.copy(this)
    }
}