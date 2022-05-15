package misc.extensions

import notices.Ticket
import purposes.Status
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.memory.memory
import screeps.utils.unsafe.jsObject
import storage.StorageHolder

var SpawnMemory.currentTicketKey by memory<String?>()
var SpawnMemory.status by memory(Status.Idle)

val StructureSpawn.purposefulSpawn: PurposefulSpawn
    get() = PurposefulSpawn(this)

var StructureSpawn.currentTicket: Ticket<*>?
    get() {
        return memory.currentTicketKey?.let { StorageHolder.tickets.get(it) }
    }
    set(value) {
        memory.currentTicketKey?.let { StorageHolder.tickets.remove(it) }
        memory.currentTicketKey = value?.let { StorageHolder.tickets.set(it) }
    }

fun StructureSpawn.spawn(roleName: String, parts: Array<BodyPartConstant>): ScreepsReturnCode {
    val creepName = "${roleName}_${Game.time}"

    return spawnCreep(parts, creepName, options {
        memory = jsObject<CreepMemory> { role = roleName }
    })
}