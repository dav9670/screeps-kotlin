package notices

import com.benasher44.uuid.uuid4
import storage.NoticeStorage
import storage.StorageHolder

class Board<R : Receiver>(private val name: String = uuid4().toString()) {
    private val notices: NoticeStorage<R>
        get() {
            @Suppress("UNCHECKED_CAST")
            return (StorageHolder.notices[name] as NoticeStorage<R>?)!!
        }

    val size: Int
        get() = notices.size

    fun addNotice(notice: Notice<*, R>): String {
        val key = notices.set(notice)
        notice.setWatcher(NoticeWatcher(this, key))
        return key
    }

    fun noticesFrom(sender: Sender): Collection<Pair<String, Notice<*, R>>> {
        return notices.noticesFrom(sender)
    }

    fun remove(key: String) {
        notices.remove(key)
    }

    fun affinities(receiver: R) : Collection<Pair<Notice<*, R>, Double>> {
        val affinities = mutableListOf<Pair<Notice<*, R>, Double>>()

        for (notice in notices.getAll()) {
            affinities.add(notice to notice.affinity(receiver))
        }

        return affinities
    }

    override fun toString(): String {
        return """
$name = {
    messages (${notices.size}) = [ 
        ${notices.getAll().joinToString("\n").prependIndent("\t")}
    ]
}
""".trimIndent()
    }
}