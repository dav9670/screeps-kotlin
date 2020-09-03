package creeps.purposefulCreeps.roles

import screeps.api.Identifiable

class MailBox<R: Identifiable> {
    enum class Priority {
        Critical,
        High,
        Medium,
        Low,
        None
    }

    private val messages = mutableMapOf(*(Priority.values().map{it to mutableListOf<Message<out Identifiable, R>>()}.toTypedArray()))

    var messageCount = 0
    private set


    val mostUrgentMessage: Message<*, R>?
    get() {
        return getMostUrgentMessage()
    }

    fun addMessage(priority: Priority, message: Message<*, R>) {
        messages[priority]!!.add(message)
        messageCount++
    }

    fun popMostUrgentMessage(): Message<*, R>? {
        return getMostUrgentMessage(true)
    }

    private fun getMostUrgentMessage(pop: Boolean = false): Message<*, R>? {
        for(priority in Priority.values()) {
            if(messages[priority]!!.isNotEmpty()) {
                val message =  messages[priority]!!.first()
                if(pop) {
                    messages[priority]!!.remove(message)
                    messageCount--
                }

                return message
            }
        }

        return null
    }
}