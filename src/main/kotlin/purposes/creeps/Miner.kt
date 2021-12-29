package purposes.creeps

import messages.Message
import messages.NeedCarryMessage
import messages.NeedSpawnMessage
import misc.extensions.availableSpots
import misc.extensions.sourceSpots
import misc.extensions.status
import misc.extensions.targetId
import purposes.Status
import screeps.api.*

class Miner(creep: Creep) : PurposefulCreep(creep) {
    companion object CreepRoleCompanion : CreepRole<Miner>(Miner::class.simpleName!!) {
        override fun spawnNeeds(): List<NeedSpawnMessage> {
            if (Miner.roleCount == 0) {
                return listOf(NeedSpawnMessage(this, Message.Priority.Critical))
            }

            if (Miner.roleCountAndSpawning < Memory.sourceSpots) {
                return listOf(NeedSpawnMessage(this, Message.Priority.Medium))
            }

            return listOf()
        }

        override val parts: Array<BodyPartConstant>
            get() = arrayOf(CARRY, WORK, MOVE)
    }

    override fun init() {
        for (source in Game.rooms.values[0].find(FIND_SOURCES)) {
            if (source.availableSpots() > 0) {
                creep.memory.targetId = source.id
                break
            }
        }

        creep.memory.status = Status.Active
    }

    override fun doRole() {
        if (creep.store.getFreeCapacity() == 0) {
            Hauler.mailBox.addMessage(NeedCarryMessage(this, Message.Priority.Medium))
            creep.memory.status = Status.Sleeping
            return
        }

        creep.harvest()
    }

    override fun respond(receiver: Identifiable, message: Message<*, *>) {
        when (message) {
            is NeedCarryMessage -> {
                creep.say("\uD83E\uDD20")

                creep.memory.status = Status.Active
            }
        }
    }

    private fun Creep.harvest() {
        val source = Game.getObjectById<Source>(creep.memory.targetId)!!
        if (harvest(source) == ERR_NOT_IN_RANGE) {
            moveTo(source.pos)
        }
    }
}