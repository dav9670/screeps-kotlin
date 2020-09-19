package creeps.purposefulCreeps.messages

import screeps.api.Identifiable

class MailBox<R : Identifiable> {
    private val messages = mutableListOf<Message<out Identifiable, R>>()

    val messageCount: Int
        get() {
            return messages.size
        }


    val mostUrgentMessage: Message<*, R>?
        get() {
            return getMostUrgentMessage()
        }

    fun addMessage(message: Message<*, R>) {
        messages.add(message)
    }

    fun popMostUrgentMessage(): Message<*, R>? {
        return getMostUrgentMessage(true)
    }

    private fun getMostUrgentMessage(pop: Boolean = false): Message<*, R>? {
        for (priority in Message.Priority.values()) {
            val messagesForPriority = messages.filter { it.priority == priority }
            if (messagesForPriority.isNotEmpty()) {
                val message = messagesForPriority.first()

                if (pop) {
                    message.repeat--
                    messages.remove(message)

                    if (message.repeat > 0) {
                        messages.add(message.copy())
                    }
                }

                return message
            }
        }

        return null
    }
}