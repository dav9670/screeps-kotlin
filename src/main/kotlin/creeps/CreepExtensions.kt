package creeps

import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.messages.Message
import creeps.purposefulCreeps.messages.storage.StorageHolder
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.utils.memory.memory

enum class Status {
    Idle, //Is not doing anything, but could start doing something on its own
    Sleeping, //Is not doing anything, will only wake up when receiving a message
    Active
}

/* Creep.memory */
var CreepMemory.role by memory { PurposefulCreep.spawnBehavior.role }
var CreepMemory.targetId by memory { "" }
var CreepMemory.initialized by memory { false }
var CreepMemory.status by memory(Status.Idle)
var CreepMemory.currentMessageKey by memory<String?>()

val Creep.purposefulCreep: PurposefulCreep
    get() {
        return when (memory.role) {
            Hauler::class.simpleName -> Hauler(id)
            Miner::class.simpleName -> Miner(id)
            else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
        }
    }

var Creep.currentMessage: Message<*, *>?
    get() {
        return memory.currentMessageKey?.let { StorageHolder.storage.getMessage(it) }
    }
    set(value) {
        memory.currentMessageKey?.let { StorageHolder.storage.removeMessage(it) }
        memory.currentMessageKey = value?.let { StorageHolder.storage.setMessage(it) }
    }
