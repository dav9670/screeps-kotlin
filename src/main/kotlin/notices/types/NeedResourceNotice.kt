package notices.types

import notices.Ticket
import purposes.PurposefulBeing
import purposes.creeps.Hauler
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import kotlin.math.min

class NeedResourceNotice(
    override val sender: PurposefulBeing,
    override val priority: Priority,
    val resourceType: ResourceConstant = RESOURCE_ENERGY,
    amount: Int = 0
) : AmountNotice<PurposefulBeing, Hauler, NeedResourceNotice, NeedResourceTicket>(amount) {
    override fun specificAffinity(receiver: Hauler): Double {
        val resourceAmount = receiver.creep.store.getUsedCapacity(resourceType)!!

        if (resourceAmount == 0)
            return 0.0

        val amountTransferred: Int = min(resourceAmount, amount)

        return (100000 - (receiver.creep.pos.getRangeTo(sender.pos).toDouble())) * amountTransferred
    }

    override fun createTicket(amount: Int): NeedResourceTicket {
        return NeedResourceTicket(this, amount)
    }
}

class NeedResourceTicket(notice: NeedResourceNotice, amount: Int) : AmountTicket<NeedResourceTicket, NeedResourceNotice>(notice, amount) {
}