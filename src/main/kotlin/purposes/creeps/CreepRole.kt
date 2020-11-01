package purposes.creeps

import messages.MailBox
import messages.Message
import messages.NeedSpawnMessage
import misc.extensions.currentMessage
import misc.extensions.purposefulCreep
import misc.extensions.role
import misc.extensions.status
import purposes.Role
import purposes.Status
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.Game
import screeps.api.values
import storage.StorageHolder

abstract class CreepRole<T : PurposefulCreep>(mailBox: MailBox<T> = MailBox()) : Role<T>(mailBox) {
    val creeps: List<Creep>
        get() = Game.creeps.values.filter { it.memory.role == roleName }
    override val purposefulBeings: List<T>
        get() = creeps.map { it.purposefulCreep.unsafeCast<T>() }
    override val availableBeings: List<T>
        get() = purposefulBeings.filter { it.creep.memory.status == Status.Idle || it.creep.memory.status == Status.Sleeping }
    open val tickBetweenSpawnNeeds: Int = 5
    val creepsWaitingToSpawn: Int
        get() = StorageHolder.messages.messagesForSender(this).filter { it.second is NeedSpawnMessage }.count()
    val roleCountAndSpawning: Int
        get() = roleCount + creepsWaitingToSpawn

    abstract val roleName: String
    abstract val parts: Array<BodyPartConstant>

    override fun messageChosen(being: T, message: Message<*, *>) {
        being.creep.currentMessage = message
        being.creep.memory.status = Status.Active
    }

    final override fun execute() {
        super.execute()

        if ((Game.time % tickBetweenSpawnNeeds) == 0) {
            spawnNeeds().forEach { PurposefulSpawn.mailBox.addMessage(it) }
        }
    }

    /**
     * @return A set of NeedSpawnMessage
     */
    abstract fun spawnNeeds(): List<NeedSpawnMessage>
}