package creeps.purposefulCreeps.roles.miner

import creeps.purposefulCreeps.roles.Message
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant

class NeedCarryMessage(override val sender: Miner, val resource: ResourceConstant = RESOURCE_ENERGY, val amount: Int = sender.creep.store.getUsedCapacity(resource)!!) : Message<Miner, Hauler> {
    override fun affinity(receiver: Hauler): Double {
        return (receiver.creep.pos.getRangeTo(sender.creep)).toDouble()
    }
}