package purposes.creeps

import notices.Notice
import misc.extensions.currentTicket
import notices.Ticket
import notices.types.*
import screeps.api.*

class Hauler(creep: Creep) : PurposefulCreep(creep) {
    companion object CreepRoleCompanion : CreepRole<Hauler>(Hauler::class.simpleName!!) {
        override fun spawnNeeds(): List<NeedSpawnNotice> {
            if (Hauler.board.size == 0) {
                return listOf()
            }

            if (Hauler.roleCountAndSpawning < (Miner.roleCount + 1) / 2) {
                return listOf(NeedSpawnNotice(this, Notice.Priority.High))
            }

            return listOf()
        }

        override val parts: Array<BodyPartConstant>
            get() = arrayOf(MOVE, CARRY)

        override fun createTicket(being: Hauler, notice: Notice<*, Hauler>): Ticket<*> {
            return when(notice){
                is NeedCarryNotice -> {
                    notice.takeTicket(mapOf(AmountNotice.Params.AMOUNT.name to being.creep.store.getFreeCapacity()))
                }
                is NeedResourceNotice -> {
                    notice.takeTicket(mapOf(AmountNotice.Params.AMOUNT.name to being.creep.store.getUsedCapacity(notice.resourceType)!!))
                }
                else -> throw Exception(Hauler::class.simpleName + " does not handle " + notice::class.simpleName + " notices")
            }
        }
    }

    override fun doRole() {
        when (val ticket = creep.currentTicket) {
            null -> {
                return
            }
            is NeedCarryTicket -> {
                if (creep.fetch(ticket.notice.sender.creep, ticket.notice.resourceType, ticket.amount)) {
                    ticket.notice.sender.respond(this, ticket)
                    onTicketFinished()
                }
            }
            is NeedResourceTicket -> {
                val resourceType = ticket.notice.resourceType
                if (creep.give(ticket.notice.sender.gameObject, resourceType, ticket.amount)) {
                    ticket.notice.sender.respond(this, ticket)
                    onTicketFinished()
                }
            }
        }
    }

    override fun respond(receiver: Identifiable, ticket: Ticket<*>) {
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