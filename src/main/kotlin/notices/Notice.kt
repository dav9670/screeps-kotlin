package notices

abstract class Notice<S : Sender, in R : Receiver> {
    abstract val sender: S
    abstract val priority: Priority

    protected lateinit var watcher: NoticeWatcher
    fun setWatcher(watcher: NoticeWatcher){
        this.watcher = watcher
    }

    /**
     * @return An affinity between 0 and 1, 0 meaning that the receiver cannot handle the task, and 1 meaning that the handler can optimally handle it
     */
    protected abstract fun specificAffinity(receiver: R): Double

    fun affinity(receiver: R): Double {
        val specAff = specificAffinity(receiver)

        return if(specAff == 0.0) 0.0 else (specAff * (priority.ceiling - priority.floor)) + priority.floor
    }

    abstract fun takeTicket(params: Map<String, Any>): Ticket<*>

    override fun toString(): String {
        return "Message(sender=$sender, priority=$priority)"
    }

    enum class Priority(val floor: Double, val ceiling: Double) {
        Critical(0.5, 1.0),
        High(0.25, 0.75),
        Medium(0.0, 0.5),
        Low(0.0, 0.25)
    }
}