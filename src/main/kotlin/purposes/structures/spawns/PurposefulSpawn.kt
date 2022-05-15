package purposes.structures.spawns

import misc.extensions.currentTicket
import misc.extensions.currentTicketKey
import notices.Notice
import notices.types.NeedResourceNotice
import notices.types.NeedSpawnNotice
import misc.extensions.spawn
import misc.extensions.status
import notices.Ticket
import notices.types.NeedSpawnTicket
import purposes.PurposefulBeing
import purposes.Status
import purposes.creeps.Hauler
import screeps.api.*
import screeps.api.structures.StructureSpawn

class PurposefulSpawn(val spawn: StructureSpawn) : PurposefulBeing, StoreOwner by spawn {
    companion object SpawnRoleCompanion : SpawnRole(PurposefulSpawn::class.simpleName!!)

    override val gameObject: StoreOwner = spawn

    override fun init() {
    }

    override fun execute() {
        when (val ticket = spawn.currentTicket) {
            null -> {
                return
            }
            is NeedSpawnTicket -> {
                // Before spawn start spawning
                if (ticket.step == NeedSpawnTicket.Step.START) {
                    when (spawn.spawn(ticket.notice.sender.roleName, ticket.notice.parts)) {
                        OK -> {
                            ticket.step = NeedSpawnTicket.Step.SPAWNING
                        }
                        ERR_NOT_ENOUGH_ENERGY -> {
                            //TODO Change amount
                            Hauler.board.addNotice(NeedResourceNotice(this, Notice.Priority.High, RESOURCE_ENERGY, 200))
                        }
                    }
                }

                if (ticket.step == NeedSpawnTicket.Step.SPAWNING && spawn.spawning == null) {
                    onTicketFinished()
                }
            }
        }
    }

    override fun onReload() {
        spawn.memory.status = Status.Idle
        spawn.memory.currentTicketKey = null
    }

    override fun respond(receiver: Identifiable, ticket: Ticket<*>) {
        TODO("Not yet implemented")
    }

    private fun onTicketFinished() {
        spawn.memory.status = Status.Idle
        spawn.currentTicket = null
    }

    override fun toString(): String {
        return "PurposefulSpawn = {$id}"
    }
}