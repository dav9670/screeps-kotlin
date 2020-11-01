package messages

import purposes.creeps.CreepRole
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.BodyPartConstant

class NeedSpawnMessage(override val sender: CreepRole<*>, override val priority: Priority, val parts: Array<BodyPartConstant> = sender.parts) : Message<CreepRole<*>, PurposefulSpawn>() {
    enum class Step {
        START,
        SPAWNING
    }

    var step: Step = Step.START

    override fun affinity(receiver: PurposefulSpawn): Double {
        return 1.0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false
        if (!super.equals(other)) return false

        other as NeedSpawnMessage

        if (sender != other.sender) return false
        if (priority != other.priority) return false
        if (!parts.contentEquals(other.parts)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + parts.contentHashCode()
        return result
    }
}