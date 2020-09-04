package creeps.purposefulCreeps.roles.miner

import creeps.purposefulCreeps.roles.Message
import creeps.purposefulCreeps.roles.hauler.Hauler
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant

class NeedCarryMessage(sender: Miner, priority: Priority, val resource: ResourceConstant = RESOURCE_ENERGY, val amount: Int = sender.creep.store.getUsedCapacity(resource)!!) : Message<Miner, Hauler>(sender, priority) {
    override fun affinity(receiver: Hauler): Double {
        return Double.Companion.MAX_VALUE - (receiver.creep.pos.getRangeTo(sender.creep)).toDouble()
    }
}