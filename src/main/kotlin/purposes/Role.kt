package purposes

import com.benasher44.uuid.uuid4
import notices.Board
import notices.Notice
import notices.Ticket

abstract class Role<T : PurposefulBeing>(val roleName: String, val board: Board<T>) : PurposefulConcept {
    abstract val purposefulBeings: List<T>
    abstract val availableBeings: List<T>
    final override val id: String = uuid4().toString()

    val roleCount: Int
        get() {
            return purposefulBeings.size
        }

    open fun handleMessages() {
        if (board.size > 0) {
            if (availableBeings.isNotEmpty()) {

            }
        }
    }

    private fun getTickets(beings: Collection<T>): Map<T, Ticket<*>> {
        // TODO This is a greedy solution, should implement A* maybe?

        val affinityMatrix = mutableListOf<Pair<Pair<T, Notice<*, T>>, Double>>()

        for(being in beings) {
            affinityMatrix.addAll(board.affinities(being).map { (being to it.first) to it.second })
        }

        affinityMatrix.sortBy { it.second }

        val tickets = mutableMapOf<T, Ticket<*>>()

        for(affinityStruct in affinityMatrix) {
            val being = affinityStruct.first.first
            val notice = affinityStruct.first.second

            // TODO Check if notice is still valid
            if(!tickets.contains(being)) {
                val ticket = createTicket(being, notice)
                tickets[being] = ticket
            }
        }

        return tickets
    }

    abstract fun createTicket(being: T, notice: Notice<*, T>): Ticket<*>

    abstract fun ticketChosen(being: T, ticket: Ticket<*>)

    override fun init() {}

    override fun execute() {}

    override fun onReload() {}

    override fun toString(): String {
        return """
            
Role = {
    id = '$id',
    roleName = '$roleName', 
    mailBox = ${board.toString().prependIndent("\t")},
    purposefulBeings = ${purposefulBeings.count()}, 
    availableBeings = ${availableBeings.count()}
}
""".trimIndent()
    }
}