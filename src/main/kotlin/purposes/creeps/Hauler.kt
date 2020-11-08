package purposes.creeps

import messages.Message
import messages.NeedCarryMessage
import messages.NeedResourceMessage
import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import screeps.api.*

class Hauler(creep: Creep) : PurposefulCreep(creep) {
    companion object CreepRoleCompanion : CreepRole<Hauler>(Hauler::class.simpleName!!) {
        override fun spawnNeeds(): List<NeedSpawnMessage> {
            if (Hauler.mailBox.messageCount == 0) {
                return listOf()
            }

            if (Hauler.roleCountAndSpawning < (Miner.roleCount + 1) / 2) {
                return listOf(NeedSpawnMessage(this, Message.Priority.High))
            }

            return listOf()
        }

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
            is NeedResourceMessage -> {
                val resourceType = message.resourceType
                val matchingResourceAvailableQuantity = creep.store.getUsedCapacity(message.resourceType)!!
                if (creep.give(message.sender.gameObject, resourceType, matchingResourceAvailableQuantity)) {
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

    private fun Creep.give(target: StoreOwner, resourceType: ResourceConstant, amount: Int): Boolean {
        if (target.pos.inRangeTo(pos, 1)) {
            transfer(target, resourceType, amount)
            return true
        }

        moveTo(target)
        return false
    }
}