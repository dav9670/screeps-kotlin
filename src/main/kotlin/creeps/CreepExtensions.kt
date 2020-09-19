package creeps

import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.messages.Message
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import screeps.api.Creep
import screeps.api.CreepMemory
import screeps.api.MemoryMarker
import screeps.utils.memory.MemoryMappingDelegate
import screeps.utils.memory.memory
import kotlin.properties.ReadWriteProperty

enum class Status {
    Idle, //Is not doing anything, but could start doing something on its own
    Sleeping, //Is not doing anything, will only wake up when receiving a message
    Active
}

/* Creep.memory */
var CreepMemory.role by memory { PurposefulCreep.spawnBehavior.role }
var CreepMemory.targetId by memory { "" }
var CreepMemory.initialized by memory { false }
var CreepMemory.status by memory(Status.Idle)
var CreepMemory.currentMessage by memoryNullableWithSerializer<Message<*, *>?>({ null }, {
    it?.serialize() ?: null.toString()
}, { if (it == null.toString()) null else Message.deserialize(it) })

val Creep.purposefulCreep: PurposefulCreep
    get() {
        return when (memory.role) {
            Hauler::class.simpleName -> Hauler(id)
            Miner::class.simpleName -> Miner(id)
            else -> throw Exception("Creep $name role (${memory.role}) is not recognized")
        }
    }

fun <T : Any?> memoryNullableWithSerializer(default: () -> T, serializer: (T) -> String, deserializer: (String) -> T)
        : ReadWriteProperty<MemoryMarker, T> = MemoryMappingDelegate(default, serializer, deserializer)
