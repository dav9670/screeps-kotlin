package creeps.purposefulCreeps

import creeps.Status
import creeps.initialized
import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.status
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.GenericCreep
import screeps.api.Identifiable

abstract class PurposefulCreep(val creep: Creep): Identifiable by creep {
    companion object RoleCompanion : Role<PurposefulCreep>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() = SpawnBehavior.SpawnPriority.NONE.priority
        override val body: Array<BodyPartConstant>
            get() = arrayOf()
        override val role: String
            get() = ""
    })

    open fun init() {}
    fun execute() {
        if(!creep.memory.initialized) {
            init()
            creep.memory.initialized = true

        }

        if(creep.memory.status != Status.Sleeping) {
            doRole()
        }
    }
    protected abstract fun doRole()
}