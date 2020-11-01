package purposes.creeps

import messages.Message
import messages.NeedCarryMessage
import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import screeps.api.*

class Hauler(creep: Creep) : PurposefulCreep(creep) {
    companion object CreepRoleCompanion : CreepRole<Hauler>() {
        override fun spawnNeeds(): List<NeedSpawnMessage> {
            if (Hauler.mailBox.messageCount == 0) {
                return listOf()
            }

            if (Hauler.roleCountAndSpawning < (Miner.roleCount + 1) / 2) {
                return listOf(NeedSpawnMessage(this, Message.Priority.High))
            }

            return listOf()
        }

        override val roleName: String
            get() = Hauler::class.simpleName!!
        override val parts: Array<BodyPartConstant>
            get() = arrayOf(MOVE, CARRY)
    }

    override fun doRole() {
        when (val message = creep.currentMessage) {
            null -> {
                return
            }
            is NeedCarryMessage -> {
                if (creep.fetch(message.sender.creep, message.resourceType, message.amount)) {
                    message.sender.respond(this, message)
                    onMessageFinished()
                }
            }
        }
    }

    override fun respond(receiver: Identifiable, message: Message<*, *>) {
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