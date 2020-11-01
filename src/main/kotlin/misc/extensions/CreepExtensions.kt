package misc.extensions

import messages.Message
import purposes.Status
import purposes.creeps.Hauler
import purposes.creeps.Miner
import purposes.creeps.PurposefulCreep
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.utils.memory.memory
import storage.StorageHolder

/* Creep.memory */
var CreepMemory.role by memory { PurposefulCreep.roleName }
var CreepMemory.targetId by memory { "" }
var CreepMemory.initialized by memory { false }
var CreepMemory.status by memory(Status.Idle)
var CreepMemory.currentMessageKey by memory<String?>()

val Creep.purposefulCreep: PurposefulCreep
    get() {
        return when (memory.role) {
            Hauler::class.simpleName -> Hauler(this)
            Miner::class.simpleName -> Miner(this)
            else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
        }
    }

var Creep.currentMessage: Message<*, *>?
    get() {
        return memory.currentMessageKey?.let { StorageHolder.messages.get(it) }
    }
    set(value) {
        memory.currentMessageKey?.let { StorageHolder.messages.remove(it) }
        memory.currentMessageKey = value?.let { StorageHolder.messages.set(it) }
    }
