package notices.types

import notices.Notice
import purposes.creeps.Hauler
import purposes.creeps.PurposefulCreep
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import kotlin.math.min

//TODO Delete message when sender dies
class NeedCarryNotice(
    override val sender: PurposefulCreep,
    override val priority: Notice.Priority,
    val resourceType: ResourceConstant = RESOURCE_ENERGY,
    amount: Int = sender.creep.store.getUsedCapacity(resourceType)!!
) : AmountNotice<PurposefulCreep, Hauler, NeedCarryNotice, NeedCarryTicket>(amount) {
    override fun specificAffinity(receiver: Hauler): Double {
        val freeCapacity = receiver.creep.store.getFreeCapacity()

        if (freeCapacity == 0)
            return 0.0

        val amountTransferred: Int = min(freeCapacity, amount)

        return 1 / ((receiver.creep.pos.getRangeTo(sender.creep)).toDouble() * amountTransferred)
    }

    override fun createTicket(amount: Int): NeedCarryTicket {
        return NeedCarryTicket(this, amount)
    }
}

class NeedCarryTicket(notice: NeedCarryNotice, amount: Int) :
    AmountTicket<NeedCarryTicket, NeedCarryNotice>(notice, amount) {
}