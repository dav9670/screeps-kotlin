package misc.extensions

import messages.Message
import purposes.Status
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.memory.memory
import screeps.utils.unsafe.jsObject
import storage.StorageHolder

var SpawnMemory.currentMessageKey by memory<String?>()
var SpawnMemory.status by memory(Status.Idle)

val StructureSpawn.purposefulSpawn: PurposefulSpawn
    get() = PurposefulSpawn(this)

var StructureSpawn.currentMessage: Message<*, *>?
    get() {
        return memory.currentMessageKey?.let { StorageHolder.messages.get(it) }
    }
    set(value) {
        memory.currentMessageKey?.let { StorageHolder.messages.remove(it) }
        memory.currentMessageKey = value?.let { StorageHolder.messages.set(it) }
    }

fun StructureSpawn.spawn(roleName: String, parts: Array<BodyPartConstant>): ScreepsReturnCode {
    val creepName = "${roleName}_${Game.time}"

    return spawnCreep(parts, creepName, options {
        memory = jsObject<CreepMemory> { role = roleName }
    })
}