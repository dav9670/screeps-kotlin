package creeps.purposefulCreeps

import creeps.Status
import creeps.currentMessage
import creeps.initialized
import creeps.purposefulCreeps.messages.Message
import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.status
import screeps.api.*
import screeps.utils.lazyPerTick

abstract class PurposefulCreep(override val id: String) : Identifiable {
    companion object RoleCompanion : Role<PurposefulCreep>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() = SpawnBehavior.SpawnPriority.NONE.priority
        override val body: Array<BodyPartConstant>
            get() = arrayOf()
        override val role: String
            get() = ""
    })

    val creep by lazyPerTick<PurposefulCreep, Creep> {
        Game.creeps.values.find { it.id == id }!!
    }

    open fun init() {}

    fun execute() {
        if (creep.spawning)
            return

        if (!creep.memory.initialized) {
            init()
            creep.memory.initialized = true
        }

        if (creep.memory.status != Status.Sleeping) {
            doRole()
        }
    }

    protected abstract fun doRole()

    open fun onReload() {
        creep.memory.status = Status.Idle
        creep.memory.currentMessage = null
    }

    abstract fun respond(message: Message<*, *>)
}