package purposes

import com.benasher44.uuid.uuid4
import messages.MailBox
import messages.Message
import screeps.api.Identifiable

abstract class Role<T : PurposefulBeing>(val mailBox: MailBox<T> = MailBox()) : PurposefulConcept {
    abstract val purposefulBeings: List<T>
    abstract val availableBeings: List<T>
    final override val id: String = uuid4().toString()

    val roleCount: Int
        get() {
            return purposefulBeings.size
        }

    fun handleMessages() {
        if (mailBox.messageCount > 0) {
            if (availableBeings.isNotEmpty()) {
                mailBox.popMostUrgentMessage { message: Message<*, T> ->
                    val bestBeing = availableBeings.maxByOrNull { message.affinity(it) }!!

                    if (message.affinity(bestBeing) == 0.0) {
                        return@popMostUrgentMessage false
                    }

                    messageChosen(bestBeing, message)

                    return@popMostUrgentMessage true
                }
            }
        }
    }

    abstract fun messageChosen(being: T, message: Message<*, *>)

    override fun respond(receiver: Identifiable, message: Message<*, *>) {}

    override fun init() {}

    override fun execute() {}

    override fun onReload() {}
}