package creeps

import creeps.purposefulCreeps.Hauler
import creeps.purposefulCreeps.Miner
import creeps.purposefulCreeps.PurposefulCreep
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.api.NavigationTarget
import screeps.api.RoomPosition
import screeps.utils.memory.memory

/* Creep.memory */
var CreepMemory.role by memory { PurposefulCreep.spawnBehavior.role }
var CreepMemory.targetId by memory { "" }

val Creep.purposefulCreep: PurposefulCreep
get() {
    return when(memory.role) {
        Hauler::class.simpleName -> Hauler(this)
        Miner::class.simpleName -> Miner(this)
        else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
    }
}