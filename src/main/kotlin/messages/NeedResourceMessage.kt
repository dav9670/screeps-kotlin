package messages

import purposes.PurposefulBeing
import purposes.creeps.Hauler
import purposes.creeps.PurposefulCreep
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import kotlin.math.min

class NeedResourceMessage(override val sender: PurposefulCreep, override val priority: Priority, val resourceType: ResourceConstant = RESOURCE_ENERGY, val amount: Int = 0) : Message<PurposefulBeing, Hauler>() {
    override fun affinity(receiver: Hauler): Double {
        val resourceAmount = receiver.creep.store.getUsedCapacity(resourceType)!!

        if (resourceAmount == 0)
            return 0.0

        val amountTransferred: Int = min(resourceAmount, amount)

        return (100000 - (receiver.creep.pos.getRangeTo(sender).toDouble())) * amountTransferred
    }
}