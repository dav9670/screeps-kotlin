package messages

import screeps.api.Identifiable

interface Sender : Identifiable {
    fun respond(receiver: Identifiable, message: Message<*, *>)
}