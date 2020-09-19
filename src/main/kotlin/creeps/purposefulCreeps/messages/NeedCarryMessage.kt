package creeps.purposefulCreeps.messages

import creeps.purposefulCreep
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.roles.hauler.Hauler
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import screeps.api.*
import screeps.utils.lazyPerTick

@Serializable
class NeedCarryMessage(override val senderId: String, override val priority: Priority, val resourceType: ResourceConstant = RESOURCE_ENERGY, val amount: Int = Game.getObjectById<Creep>(senderId)!!.store.getUsedCapacity(resourceType)!!) : Message<PurposefulCreep, Hauler>() {
    override val sender by lazyPerTick<NeedCarryMessage, PurposefulCreep> {
        Game.creeps.values.find { it.id == senderId }!!.purposefulCreep
    }

    override fun affinity(receiver: Hauler): Double {
        return Double.Companion.MAX_VALUE - (receiver.creep.pos.getRangeTo(sender.creep)).toDouble()
    }

    override fun serialize(): String {
        return Json.encodeToString(this)
    }
}