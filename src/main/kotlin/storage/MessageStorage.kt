package storage

import com.benasher44.uuid.uuid4
import messages.Message
import messages.Sender

class MessageStorage : MemoryStorage<String, Message<*, *>>() {
    override val tableName: String
        get() = Message::class.simpleName!!

    private val senderMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun generateKey(value: Message<*, *>): String {
        return uuid4().toString()
    }

    override fun set(value: Message<*, *>, key: String): String {
        super.set(value, key)
        senderMap[value.sender.id] = senderMap[value.sender.id]?.apply { add(key) } ?: mutableListOf(key)

        return key
    }

    override fun remove(key: String) {
        val message = get(key)

        message?.let {
            senderMap[message.sender.id]!!.remove(key)
        }

        super.remove(key)
    }

    fun messagesFrom(sender: Sender): Collection<Pair<String, Message<*, *>>> {
        val messages = senderMap[sender.id]?.map { it to map[it]!! }

        return messages ?: emptyList()
    }
}