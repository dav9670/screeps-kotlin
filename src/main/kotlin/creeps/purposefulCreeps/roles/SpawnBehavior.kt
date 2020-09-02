package creeps.purposefulCreeps.roles

import screeps.api.BodyPartConstant

interface SpawnBehavior {
    val spawnPriority: Double
    val body: Array<BodyPartConstant>
    val role: String

    enum class SpawnPriority(val priority: Double) {
        NONE(0.0),
        SMALL(0.2),
        MEDIUM(0.5),
        HIGH(0.7),
        CRITICAL(1.0)
    }
}