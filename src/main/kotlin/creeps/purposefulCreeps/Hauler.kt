package creeps.purposefulCreeps

import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import screeps.api.*

class Hauler(creep: Creep) : PurposefulCreep(creep) {
    companion object RoleCompanion : Role<Hauler>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() {
                if(Hauler.roleCount == 0 && Miner.roleCount != 0) {
                    return SpawnBehavior.SpawnPriority.HIGH.priority
                }

                if(Hauler.roleCount < Game.spawns.values.sumBy { it.room.find(FIND_SOURCES).count() }) {
                    return SpawnBehavior.SpawnPriority.MEDIUM.priority
                }

                return SpawnBehavior.SpawnPriority.NONE.priority
            }
        override val body: Array<BodyPartConstant>
            get() {
                return arrayOf(MOVE, CARRY)
            }
        override val role: String
            get() = Hauler::class.simpleName!!
    })

    override fun execute() {
        creep.haul()

    }

    private fun Creep.haul() {

    }
}