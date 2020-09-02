package creeps.purposefulCreeps

import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import screeps.api.*

class Miner(creep: Creep) : PurposefulCreep(creep) {
    companion object RoleCompanion : Role<Miner>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() {
                if (Miner.roleCount == 0) {
                    return SpawnBehavior.SpawnPriority.HIGH.priority
                }

                if (Miner.roleCount < Game.spawns.values.sumBy { it.room.find(FIND_SOURCES).count() }) {
                    return SpawnBehavior.SpawnPriority.MEDIUM.priority
                }

                return SpawnBehavior.SpawnPriority.NONE.priority
            }
        override val body: Array<BodyPartConstant>
            get() {
                return arrayOf(MOVE, CARRY, WORK)
            }
        override val role: String
            get() = Miner::class.simpleName!!
    })

    override fun execute() {
        creep.harvest()
    }

    private fun Creep.harvest(fromRoom: Room = this.room, toRoom: Room = this.room) {
        if (store[RESOURCE_ENERGY] < store.getCapacity()) {
            val sources = fromRoom.find(FIND_SOURCES)
            if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
                moveTo(sources[0].pos)
            }
        } else {
            val targets = toRoom.find(FIND_MY_STRUCTURES)
                    .filter { (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) }
                    .map { it.unsafeCast<StoreOwner>() }
                    .filter { it.store[RESOURCE_ENERGY] < it.store.getCapacity() }

            if (targets.isNotEmpty()) {
                if (transfer(targets[0], RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                    moveTo(targets[0].pos)
                }
            }
        }
    }
}