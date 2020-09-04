package creeps.purposefulCreeps.roles.hauler

import creeps.currentMessage
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.purposefulCreeps.roles.miner.Miner
import creeps.purposefulCreeps.roles.miner.NeedCarryMessage
import screeps.api.*

class Hauler(creep: Creep) : PurposefulCreep(creep) {
    companion object RoleCompanion : Role<Hauler>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() {
                if (Hauler.roleCount < (Miner.roleCount + 1) / 2) {
                    return SpawnBehavior.SpawnPriority.HIGH.priority
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

    override fun doRole() {
        when (val message = creep.memory.currentMessage) {
            null -> return
            is NeedCarryMessage -> creep.fetch(message.sender.creep, message.resource, message.amount)
        }
    }

    private fun Creep.fetch(target: Creep, resource: ResourceConstant, amount: Int) {

    }
}