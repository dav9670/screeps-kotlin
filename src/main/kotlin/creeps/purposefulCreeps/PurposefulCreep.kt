package creeps.purposefulCreeps

import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import screeps.api.BodyPartConstant
import screeps.api.Creep

abstract class PurposefulCreep(val creep: Creep) {
    companion object RoleCompanion: Role<PurposefulCreep>(object: SpawnBehavior{
        override val spawnPriority: Double
            get() = SpawnBehavior.SpawnPriority.NONE.priority
        override val body: Array<BodyPartConstant>
            get() = arrayOf()
        override val role: String
            get() = ""
    })

    abstract fun execute()
}