package creeps.purposefulCreeps.roles

import creeps.*
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.messages.MailBox
import creeps.purposefulCreeps.messages.Message
import screeps.api.Game
import screeps.api.values
import screeps.utils.lazyPerTick

open class Role<T : PurposefulCreep>(val spawnBehavior: SpawnBehavior, val mailBox: MailBox<T> = MailBox()) {
    val creeps by lazyPerTick { Game.creeps.values.filter { it.memory.role == spawnBehavior.role }.toTypedArray() }

    val purposefulCreeps by lazyPerTick { creeps.map { it.purposefulCreep.unsafeCast<T>() }.toTypedArray() }

    val roleCount: Int
        get() {
            return creeps.size
        }

    open fun handleMessages() {
        if (mailBox.messageCount > 0) {
            val availableCreeps = purposefulCreeps.filter { it.creep.memory.status == Status.Idle || it.creep.memory.status == Status.Sleeping }

            if (availableCreeps.isNotEmpty()) {
                mailBox.popMostUrgentMessage { message: Message<*, T> ->
                    val bestCreep = availableCreeps.maxByOrNull { message.affinity(it) }!!

                    if (message.affinity(bestCreep) == 0.0) {
                        return@popMostUrgentMessage false
                    }

                    bestCreep.creep.currentMessage = message
                    bestCreep.creep.memory.status = Status.Active

                    return@popMostUrgentMessage true
                }
            }
        }
    }

    open fun onReload() {
        for (creep in purposefulCreeps) {
            creep.onReload()
        }
    }
}