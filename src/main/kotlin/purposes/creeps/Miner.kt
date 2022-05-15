package purposes.creeps

import notices.Notice
import notices.types.NeedCarryNotice
import notices.types.NeedSpawnNotice
import misc.extensions.availableSpots
import misc.extensions.sourceSpots
import misc.extensions.status
import misc.extensions.targetId
import notices.Ticket
import notices.types.NeedCarryTicket
import purposes.Status
import screeps.api.*

class Miner(creep: Creep) : PurposefulCreep(creep) {
    companion object CreepRoleCompanion : CreepRole<Miner>(Miner::class.simpleName!!) {
        override fun spawnNeeds(): List<NeedSpawnNotice> {
            if (Miner.roleCount == 0) {
                return listOf(NeedSpawnNotice(this, Notice.Priority.Critical))
            }

            if (Miner.roleCountAndSpawning < Memory.sourceSpots) {
                return listOf(NeedSpawnNotice(this, Notice.Priority.Medium))
            }

            return listOf()
        }

        override val parts: Array<BodyPartConstant>
            get() = arrayOf(CARRY, WORK, MOVE)

        override fun createTicket(being: Miner, notice: Notice<*, Miner>): Ticket<*> {
            TODO("Need to implement notice for miners")
        }
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
            Hauler.board.addNotice(NeedCarryNotice(this, Notice.Priority.Medium))
            creep.memory.status = Status.Sleeping
            return
        }

        creep.harvest()
    }

    override fun respond(receiver: Identifiable, ticket: Ticket<*>) {
        when (ticket) {
            is NeedCarryTicket -> {
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