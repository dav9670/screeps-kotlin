package creeps.purposefulCreeps.messages

import screeps.api.Identifiable


class MailBox<R : Identifiable> {
    private val messages = mutableListOf<Message<*, R>>()

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

    fun popMostUrgentMessage(removeIf: (message: Message<*, R>) -> Boolean = { true }): Message<*, R>? {
        val message = mostUrgentMessage
        if (message != null && removeIf(message))
            removeMessage(message)
        return message
    }

    // TODO Sort on add instead of sorting at each getMostUrgentMessage
    private fun getMostUrgentMessage(): Message<*, R>? {
        for (priority in Message.Priority.values()) {
            val messagesForPriority = messages.filter { it.priority == priority }
            if (messagesForPriority.isNotEmpty()) {
                return messagesForPriority.first()
            }
        }

        return null
    }

    private fun removeMessage(message: Message<*, R>) {
        message.repeat--
        messages.remove(message)

        if (message.repeat > 0) {
            messages.add(message.copy())
        }
    }
}