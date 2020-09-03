package creeps.purposefulCreeps.roles

import creeps.Status
import creeps.currentMessage
import creeps.purposefulCreeps.PurposefulCreep
import creeps.role
import creeps.status
import screeps.api.Creep
import screeps.api.Game
import screeps.api.values

open class Role<T : PurposefulCreep>(val spawnBehavior: SpawnBehavior) {
    val mailBox = MailBox<T>()

    val creeps: Array<Creep>
        get() {
            return Game.creeps.values.filter { it.memory.role == spawnBehavior.role }.toTypedArray()
        }

    val purposefulCreeps: Array<T>
        get() {
            return creeps.map { it.unsafeCast<T>() }.toTypedArray()
        }

    val roleCount: Int
        get() {
            return creeps.size
        }

    open fun handleMessages() {
        if(mailBox.messageCount > 0) {
            val availableCreeps = purposefulCreeps.filter { it.creep.memory.status == Status.Idle || it.creep.memory.status == Status.Sleeping }

            if(availableCreeps.isNotEmpty()) {
                val message = mailBox.popMostUrgentMessage()!!
                val bestCreep = availableCreeps.minBy { message.affinity(it) }!!

                bestCreep.creep.memory.currentMessage = message
                bestCreep.creep.memory.status = Status.Active
            }
        }
    }
}