package creeps

import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.roles.Message
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.api.Identifiable
import screeps.utils.memory.memory

enum class Status {
    Idle, //Is not doing anything, but could start doing something on its own
    Sleeping, //Is not doing anything, will only wake up when receiving a message
    Active
}

/* Creep.memory */
var CreepMemory.role by memory { PurposefulCreep.spawnBehavior.role }
var CreepMemory.targetId by memory { "" }
var CreepMemory.initialized  by memory { false }
var CreepMemory.status by memory { Status.Idle }
var CreepMemory.currentMessage by memory<Message<out Identifiable, *>?>()

val Creep.purposefulCreep: PurposefulCreep
    get() {
        return when (memory.role) {
            Hauler::class.simpleName -> Hauler(this)
            Miner::class.simpleName -> Miner(this)
            else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
        }
    }