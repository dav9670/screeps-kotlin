package messages

import storage.StorageHolder


class MailBox<R : Receiver> {
    private val messageKeys = mutableListOf<String>()
    private val messages: Map<String, Message<*, R>>
        get() = messageKeys.map { it to getMessage(it) }.toMap()

    val messageCount: Int
        get() {
            return messageKeys.size
        }

    private val mostUrgentMessageKey: String?
        get() {
            for (priority in Message.Priority.values()) {
                val messagesForPriority = messages.filter { it.value.priority == priority }
                if (messagesForPriority.isNotEmpty()) {
                    return messagesForPriority.entries.first().key
                }
            }

            return null
        }

    fun addMessage(message: Message<*, R>) {
        val key = StorageHolder.messages.set(message)
        messageKeys.add(key)
    }

    fun popMostUrgentMessage(removeIf: (message: Message<*, R>) -> Boolean = { true }): Message<*, R>? {
        mostUrgentMessageKey?.let {
            val message = getMessage(it)
            if (removeIf(message))
                removeMessage(it)
            return message
        }

        return null
    }

    @Suppress("UNCHECKED_CAST")
    private fun getMessage(key: String): Message<*, R> {
        return StorageHolder.messages.get(key)!! as Message<*, R>
    }

    private fun removeMessage(key: String) {
        StorageHolder.messages.remove(key)
        messageKeys.remove(key)
    }

    override fun toString(): String {
        return "MailBox(messageKeys=$messageKeys)"
    }
}