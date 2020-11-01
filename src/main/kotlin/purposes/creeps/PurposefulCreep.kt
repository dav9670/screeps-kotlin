package purposes.creeps

import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import misc.extensions.currentMessageKey
import misc.extensions.initialized
import misc.extensions.status
import purposes.PurposefulBeing
import purposes.Status
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.Identifiable
import screeps.api.RoomObject

abstract class PurposefulCreep(val creep: Creep) : Identifiable by creep, RoomObject by creep, PurposefulBeing {
    companion object CreepRoleCompanion : CreepRole<PurposefulCreep>() {
        override fun spawnNeeds(): List<NeedSpawnMessage> {
            return listOf()
        }

        override val roleName: String
            get() = PurposefulCreep::class.simpleName!!
        override val parts: Array<BodyPartConstant>
            get() = arrayOf()
    }

    override fun init() {}

    override fun execute() {
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

    override fun onReload() {
        creep.memory.status = Status.Idle
        creep.memory.currentMessageKey = null
    }

    open fun onMessageFinished() {
        creep.memory.status = Status.Idle
        creep.currentMessage = null
    }
}