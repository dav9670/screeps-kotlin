package notices.types

import notices.Ticket
import purposes.creeps.CreepRole
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.BodyPartConstant

class NeedSpawnNotice(
    override val sender: CreepRole<*>,
    override val priority: Priority,
    val parts: Array<BodyPartConstant> = sender.parts
) : AmountNotice<CreepRole<*>, PurposefulSpawn, NeedSpawnNotice, NeedSpawnTicket>() {

    override fun specificAffinity(receiver: PurposefulSpawn): Double {
        return 1.0
    }

    override fun createTicket(amount: Int): NeedSpawnTicket {
        return NeedSpawnTicket(this, amount)
    }
}

class NeedSpawnTicket(notice: NeedSpawnNotice, amount: Int): AmountTicket<NeedSpawnTicket, NeedSpawnNotice>(notice, amount) {
    enum class Step {
        START,
        SPAWNING
    }

    var step: Step = Step.START
}