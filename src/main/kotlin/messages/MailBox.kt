package messages

import com.benasher44.uuid.uuid4
import storage.StorageHolder


class MailBox<R : Receiver>(private val name: String = uuid4().toString()) {
    private val messageKeys = mutableListOf<String>()
    private val messages: Map<String, Message<*, R>>
        get() = messageKeys.associateWith { getMessage(it) }

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

    fun keyExists(key: String) : Boolean{
        return messageKeys.contains(key)
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
        val text =
                """

$name = {
    messages (${messageKeys.count()}) = [ 
${messages.values.joinToString("\n").prependIndent("\t")}
    ]
}
""".trimIndent()

        return text
    }
}