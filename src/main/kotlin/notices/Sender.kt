package notices

import screeps.api.Identifiable

interface Sender : Identifiable {
    fun respond(receiver: Identifiable, ticket: Ticket<*>)
}