package creeps.purposefulCreeps.messages

import creeps.purposefulCreep
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.roles.hauler.Hauler
import screeps.api.*
import kotlin.math.min

//TODO Delete message when sender dies
class NeedCarryMessage(override val senderId: String, override val priority: Priority, val resourceType: ResourceConstant = RESOURCE_ENERGY, val amount: Int = Game.getObjectById<Creep>(senderId)!!.store.getUsedCapacity(resourceType)!!) : Message<PurposefulCreep, Hauler>() {
    override val sender: PurposefulCreep
        get() {
            return Game.creeps.values.find { it.id == senderId }!!.purposefulCreep
        }

    override fun affinity(receiver: Hauler): Double {
        val freeCapacity = receiver.creep.store.getFreeCapacity()

        if (freeCapacity == 0)
            return 0.0

        val amountTransferred: Int = min(freeCapacity, amount)

        return (100000 - (receiver.creep.pos.getRangeTo(sender.creep)).toDouble()) * amountTransferred
    }
}