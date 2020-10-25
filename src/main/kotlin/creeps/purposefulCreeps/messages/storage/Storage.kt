package creeps.purposefulCreeps.messages.storage

import com.benasher44.uuid.uuid4
import creeps.purposefulCreeps.messages.Message


class Storage {
    private val messages: MutableMap<String, Message<*, *>> = mutableMapOf()

    fun getMessage(key: String): Message<*, *>? {
        return messages[key]
    }

    fun setMessage(message: Message<*, *>): String {
        val key = generateKey()
        messages[key] = message
        return key
    }

    fun removeMessage(key: String) {
        messages.remove(key)
    }

    private fun generateKey(): String {
        return uuid4().toString()
    }
}