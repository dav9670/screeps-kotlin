package storage

import com.benasher44.uuid.uuid4
import messages.Message
import messages.Sender

class MessageStorage : MemoryStorage<String, Message<*, *>>() {
    override val tableName: String
        get() = Message::class.simpleName!!

    private val senderMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun generateKey(): String {
        return uuid4().toString()
    }

    override fun set(value: Message<*, *>): String {
        val key = super.set(value)
        console.log("set key: " + key + ", sender: " + value.sender.id)
        senderMap[value.sender.id] = senderMap[value.sender.id]?.apply { add(key) } ?: mutableListOf(key)

        return key
    }

    override fun remove(key: String) {
        val message = get(key)

        message?.let {
            console.log("remove key: " + key + ", sender: " + message.sender.id)
            senderMap[message.sender.id]!!.remove(key)
        }

        super.remove(key)
    }

    fun messagesForSender(sender: Sender): List<Pair<String, Message<*, *>>> {
        val messages = senderMap[sender.id]?.map { it to map[it]!! }

        return messages ?: listOf()
    }
}