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
import screeps.api.StoreOwner

abstract class PurposefulCreep(val creep: Creep) : StoreOwner by creep, PurposefulBeing {
    companion object CreepRoleCompanion : CreepRole<PurposefulCreep>(PurposefulCreep::class.simpleName!!) {
        override fun spawnNeeds(): List<NeedSpawnMessage> {
            return listOf()
        }

        override val parts: Array<BodyPartConstant>
            get() = arrayOf()
    }

    override val gameObject: StoreOwner = creep

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

    override fun toString(): String {
        return "PurposefulCreep = {$id}"
    }
}