package creeps.purposefulCreeps.roles.hauler

import creeps.currentMessage
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.messages.Message
import creeps.purposefulCreeps.messages.NeedCarryMessage
import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.purposefulCreeps.roles.miner.Miner
import screeps.api.*

class Hauler(id: String) : PurposefulCreep(id) {
    companion object RoleCompanion : Role<Hauler>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() {
                if (Hauler.mailBox.messageCount == 0) {
                    return SpawnBehavior.SpawnPriority.NONE.priority
                }

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
            null -> {
                return
            }
            is NeedCarryMessage -> {
                console.log(message.sender)

                if (creep.fetch(message.sender.creep, message.resourceType, message.amount)) {
                    message.sender.respond(message)
                }
            }
        }
    }

    override fun respond(message: Message<*, *>) {
        TODO("Not yet implemented")
    }

    private fun Creep.fetch(target: Creep, resourceType: ResourceConstant, amount: Int): Boolean {
        if (target.pos.inRangeTo(pos, 1)) {
            target.transfer(this, resourceType, amount)
            return true
        }

        moveTo(target)
        return false
    }
}