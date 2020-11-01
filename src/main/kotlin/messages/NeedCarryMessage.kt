package messages

import purposes.creeps.Hauler
import purposes.creeps.PurposefulCreep
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import kotlin.math.min

//TODO Delete message when sender dies
class NeedCarryMessage(override val sender: PurposefulCreep, override val priority: Priority, val resourceType: ResourceConstant = RESOURCE_ENERGY, val amount: Int = sender.creep.store.getUsedCapacity(resourceType)!!) : Message<PurposefulCreep, Hauler>() {
    override fun affinity(receiver: Hauler): Double {
        val freeCapacity = receiver.creep.store.getFreeCapacity()

        if (freeCapacity == 0)
            return 0.0

        val amountTransferred: Int = min(freeCapacity, amount)

        return (100000 - (receiver.creep.pos.getRangeTo(sender.creep)).toDouble()) * amountTransferred
    }
}