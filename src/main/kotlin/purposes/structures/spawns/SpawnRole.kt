package purposes.structures.spawns

import messages.MailBox
import messages.Message
import misc.extensions.currentMessage
import misc.extensions.purposefulSpawn
import misc.extensions.status
import purposes.Role
import purposes.Status
import screeps.api.Game
import screeps.api.values

open class SpawnRole(mailBox: MailBox<PurposefulSpawn> = MailBox()) : Role<PurposefulSpawn>(mailBox) {
    override val purposefulBeings: List<PurposefulSpawn>
        get() = Game.spawns.values.map { it.purposefulSpawn }
    override val availableBeings: List<PurposefulSpawn>
        get() = purposefulBeings.filter { it.spawn.memory.status == Status.Idle || it.spawn.memory.status == Status.Sleeping }

    override fun messageChosen(being: PurposefulSpawn, message: Message<*, *>) {
        being.spawn.currentMessage = message
        being.spawn.memory.status = Status.Active
    }
}