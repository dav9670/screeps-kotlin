package creeps.purposefulCreeps.roles.miner

import creeps.Status
import creeps.purposefulCreeps.PurposefulCreep
import creeps.purposefulCreeps.roles.MailBox
import creeps.purposefulCreeps.roles.Role
import creeps.purposefulCreeps.roles.SpawnBehavior
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.status
import creeps.targetId
import memory.sourceSpots
import screeps.api.*
import structures.sources.availableSpots

class Miner(creep: Creep) : PurposefulCreep(creep) {
    companion object RoleCompanion : Role<Miner>(object : SpawnBehavior {
        override val spawnPriority: Double
            get() {
                if (Miner.roleCount == 0) {
                    return SpawnBehavior.SpawnPriority.HIGH.priority
                }

                if (Miner.roleCount < Memory.sourceSpots) {
                    return SpawnBehavior.SpawnPriority.MEDIUM.priority
                }

                return SpawnBehavior.SpawnPriority.NONE.priority
            }
        override val body: Array<BodyPartConstant>
            get() {
                return arrayOf(CARRY, WORK, MOVE)
            }
        override val role: String
            get() = Miner::class.simpleName!!
    })

    override fun init() {
        for (source in Game.rooms.values[0].find(FIND_SOURCES)) {
            if (source.availableSpots() > 0) {
                creep.memory.targetId = source.id
                break
            }
        }

        creep.memory.status = Status.Active
    }

    override fun doRole() {
        if (creep.store.getFreeCapacity() == 0) {
            Hauler.mailBox.addMessage(MailBox.Priority.Medium, NeedCarryMessage(this))
            return
        }

        creep.harvest()
    }

    private fun Creep.harvest() {
        val source = Game.getObjectById<Source>(creep.memory.targetId)!!
        if (harvest(source) == ERR_NOT_IN_RANGE) {
            moveTo(source.pos)
        }
    }
}