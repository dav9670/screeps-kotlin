package purposes.structures.spawns

import messages.Message
import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import misc.extensions.currentMessageKey
import misc.extensions.spawn
import misc.extensions.status
import purposes.PurposefulBeing
import purposes.Status
import screeps.api.ERR_NOT_ENOUGH_ENERGY
import screeps.api.Identifiable
import screeps.api.OK
import screeps.api.RoomObject
import screeps.api.structures.StructureSpawn

class PurposefulSpawn(val spawn: StructureSpawn) : Identifiable by spawn, RoomObject by spawn, PurposefulBeing {
    companion object SpawnRoleCompanion : SpawnRole()

    override fun respond(receiver: Identifiable, message: Message<*, *>) {
        TODO("Not yet implemented")
    }

    override fun init() {
    }

    override fun execute() {
        when (val message = spawn.currentMessage) {
            null -> {
                return
            }
            is NeedSpawnMessage -> {
                // Before spawn start spawning
                if (message.step == NeedSpawnMessage.Step.START) {
                    when (spawn.spawn(message.sender.roleName, message.parts)) {
                        OK -> {
                            message.step = NeedSpawnMessage.Step.SPAWNING
                        }
                        ERR_NOT_ENOUGH_ENERGY -> {

                        }
                    }
                }

                if (message.step == NeedSpawnMessage.Step.SPAWNING && spawn.spawning == null) {
                    onMessageFinished()
                }
            }
        }
    }

    override fun onReload() {
        spawn.memory.status = Status.Idle
        spawn.memory.currentMessageKey = null
    }

    private fun onMessageFinished() {
        spawn.memory.status = Status.Idle
        spawn.currentMessage = null
    }
}