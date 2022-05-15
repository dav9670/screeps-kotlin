package notices.types

import notices.Notice
import notices.Receiver
import notices.Sender
import notices.Ticket

abstract class AmountNotice<S: Sender, R: Receiver, Self: AmountNotice<S, R, Self, T>, T: AmountTicket<T, Self>>(amount: Int = 1) : Notice<S, R>() {
    var amount: Int = amount; private set

    protected abstract fun createTicket(amount: Int): T

    /*
    params: {"AMOUNT": Int}
     */
    override fun takeTicket(params: Map<String, Any>): T {
        val amount = params[Params.AMOUNT.name] as Int
        this.amount -= amount
        if(this.amount <= 0) {
            watcher.noticeEmpty()
        }
        return createTicket(amount)
    }

    enum class Params {
        AMOUNT
    }
}

abstract class AmountTicket<Self: AmountTicket<Self, N>, N: AmountNotice<*, *, N, Self>>(notice: N, val amount: Int) : Ticket<N>(notice) {
}