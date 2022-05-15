package purposes.creeps

import misc.extensions.currentTicket
import notices.Board
import notices.types.NeedSpawnNotice
import misc.extensions.purposefulCreep
import misc.extensions.role
import misc.extensions.status
import notices.Ticket
import purposes.Role
import purposes.Status
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.*

abstract class CreepRole<T : PurposefulCreep>(roleName: String, board: Board<T> = Board(roleName)) : Role<T>(roleName, board) {
    val creeps: List<Creep>
        get() = Game.creeps.values.filter { it.memory.role == roleName }
    override val purposefulBeings: List<T>
        get() = creeps.map { it.purposefulCreep.unsafeCast<T>() }
    override val availableBeings: List<T>
        get() = purposefulBeings.filter { it.creep.memory.status == Status.Idle || it.creep.memory.status == Status.Sleeping }
    open val tickBetweenSpawnNeeds: Int = 5
    // TODO Will only count message on board, not message accepted
    private val creepsWaitingToSpawn: Int
        get() = PurposefulSpawn.board.noticesFrom(this).size
    val roleCountAndSpawning: Int
        get() = roleCount + creepsWaitingToSpawn

    abstract val parts: Array<BodyPartConstant>

    override fun ticketChosen(being: T, ticket: Ticket<*>) {
        being.creep.currentTicket = ticket
        being.creep.memory.status = Status.Active
    }

    final override fun execute() {
        super.execute()

        if ((Game.time % tickBetweenSpawnNeeds) == 0) {
            spawnNeeds().forEach { PurposefulSpawn.board.addNotice(it) }
        }
    }

    abstract fun spawnNeeds(): List<NeedSpawnNotice>

    override fun respond(receiver: Identifiable, ticket: Ticket<*>) {
        TODO("Not yet implemented")
    }
}