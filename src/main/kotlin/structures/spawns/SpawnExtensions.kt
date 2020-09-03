package structures.spawns

import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.role
import screeps.api.*
import screeps.api.structures.StructureSpawn

fun StructureSpawn.spawn(behavior: SpawnBehavior) {
    val creepName = "${behavior.role}_${Game.time}"
    val code = spawnCreep(behavior.body, creepName, options {
        memory = screeps.utils.unsafe.jsObject<CreepMemory> { role = behavior.role }
    })

    when (code) {
        OK, ERR_BUSY, ERR_NOT_ENOUGH_ENERGY -> run { } // do nothing
        else -> console.log("unhandled error code $code")
    }
}