package storage

import com.benasher44.uuid.uuid4
import notices.Ticket

class TicketStorage : MemoryStorage<String, Ticket<*>>() {
    override val tableName: String
        get() = Ticket::class.simpleName!!

    override fun generateKey(value: Ticket<*>): String {
        return uuid4().toString()
    }
}