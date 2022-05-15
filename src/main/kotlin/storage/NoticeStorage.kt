package storage

import com.benasher44.uuid.uuid4
import notices.Notice
import notices.Receiver
import notices.Sender

class NoticeStorage<R: Receiver> : MemoryStorage<String, Notice<*, R>>() {
    override val tableName: String
        get() = Notice::class.simpleName!!

    private val senderMap: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun generateKey(value: Notice<*, R>): String {
        return uuid4().toString()
    }

    override fun set(value: Notice<*, R>, key: String): String {
        super.set(value, key)
        senderMap[value.sender.id] = senderMap[value.sender.id]?.apply { add(key) } ?: mutableListOf(key)

        return key
    }

    override fun remove(key: String) {
        val notice = get(key)

        notice?.let {
            senderMap[notice.sender.id]!!.remove(key)
        }

        super.remove(key)
    }

    fun noticesFrom(sender: Sender): Collection<Pair<String, Notice<*, R>>> {
        val notices = senderMap[sender.id]?.map { it to map[it]!! }

        return notices ?: emptyList()
    }
}