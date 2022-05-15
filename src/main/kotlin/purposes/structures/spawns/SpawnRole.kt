package purposes.structures.spawns

import misc.extensions.currentTicket
import notices.Board
import notices.Notice
import misc.extensions.purposefulSpawn
import misc.extensions.status
import notices.Ticket
import notices.types.AmountNotice
import notices.types.NeedSpawnNotice
import purposes.Role
import purposes.Status
import screeps.api.Game
import screeps.api.Identifiable
import screeps.api.values

open class SpawnRole(roleName: String, board: Board<PurposefulSpawn> = Board(roleName)) : Role<PurposefulSpawn>(roleName, board) {
    override val purposefulBeings: List<PurposefulSpawn>
        get() = Game.spawns.values.map { it.purposefulSpawn }
    override val availableBeings: List<PurposefulSpawn>
        get() = purposefulBeings.filter { it.spawn.memory.status == Status.Idle || it.spawn.memory.status == Status.Sleeping }

    override fun respond(receiver: Identifiable, ticket: Ticket<*>) {
        TODO("Not yet implemented")
    }

    override fun createTicket(being: PurposefulSpawn, notice: Notice<*, PurposefulSpawn>): Ticket<*> {
        when(notice) {
            is NeedSpawnNotice -> {
                return notice.takeTicket(mapOf(AmountNotice.Params.AMOUNT.name to 1))
            }
            else -> throw Exception(PurposefulSpawn::class.simpleName + " does not handle " + notice::class.simpleName + " notices")
        }
    }

    override fun ticketChosen(being: PurposefulSpawn, ticket: Ticket<*>) {
        being.spawn.currentTicket = ticket
        being.spawn.memory.status = Status.Active
    }
}