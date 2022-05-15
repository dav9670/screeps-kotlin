package misc.extensions

import notices.Ticket
import purposes.Status
import purposes.creeps.Hauler
import purposes.creeps.Miner
import purposes.creeps.PurposefulCreep
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.utils.memory.memory
import storage.StorageHolder

/* Creep.memory */
var CreepMemory.role by memory { "" }
var CreepMemory.targetId by memory { "" }
var CreepMemory.status by memory(Status.Created)
var CreepMemory.currentTicketKey by memory<String?>()

val Creep.purposefulCreep: PurposefulCreep
    get() {
        return when (memory.role) {
            Hauler::class.simpleName -> Hauler(this)
            Miner::class.simpleName -> Miner(this)
            else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
        }
    }

var Creep.currentTicket: Ticket<*>?
    get() {
        return memory.currentTicketKey?.let { StorageHolder.tickets.get(it) }
    }
    set(value) {
        memory.currentTicketKey?.let { StorageHolder.tickets.remove(it) }
        memory.currentTicketKey = value?.let { StorageHolder.tickets.set(it) }
    }
