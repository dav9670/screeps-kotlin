package purposes.creeps

import misc.extensions.currentTicket
import misc.extensions.currentTicketKey
import misc.extensions.status
import purposes.PurposefulBeing
import purposes.Status
import screeps.api.Creep
import screeps.api.Game
import screeps.api.StoreOwner
import screeps.api.get
import screeps.utils.lazyPerTick

abstract class PurposefulCreep(creep: Creep) : PurposefulBeing, StoreOwner by creep {
    private val creepName: String = creep.name

    val creep: Creep by lazyPerTick { Game.creeps[creepName]!! }

    override val gameObject: StoreOwner = creep

    override fun init() {}

    override fun execute() {
        if (creep.spawning)
            return

        when (creep.memory.status) {
            Status.Created -> {
                init()
                creep.memory.status = Status.Idle
            }
            Status.Active -> {
                doRole()
            }
        }
    }

    protected abstract fun doRole()

    override fun onReload() {
        creep.memory.status = Status.Idle
        creep.memory.currentTicketKey = null
    }

    open fun onTicketFinished() {
        creep.memory.status = Status.Idle
        creep.currentTicket = null
    }

    override fun toString(): String {
        return "PurposefulCreep = {$id}"
    }
}