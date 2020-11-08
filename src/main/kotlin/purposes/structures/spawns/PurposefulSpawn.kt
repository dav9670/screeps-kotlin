package purposes.structures.spawns

import messages.Message
import messages.NeedResourceMessage
import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import misc.extensions.currentMessageKey
import misc.extensions.spawn
import misc.extensions.status
import purposes.PurposefulBeing
import purposes.Status
import purposes.creeps.Hauler
import screeps.api.*
import screeps.api.structures.StructureSpawn

class PurposefulSpawn(val spawn: StructureSpawn) : StoreOwner by spawn, PurposefulBeing {
    companion object SpawnRoleCompanion : SpawnRole(PurposefulSpawn::class.simpleName!!)

    override val gameObject: StoreOwner = spawn

    override fun respond(receiver: Identifiable, message: Message<*, *>) {
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
                            //TODO Change amount
                            Hauler.mailBox.addMessage(NeedResourceMessage(this, Message.Priority.High, RESOURCE_ENERGY, 200))
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

    override fun toString(): String {
        return "PurposefulSpawn = {$id}"
    }
}