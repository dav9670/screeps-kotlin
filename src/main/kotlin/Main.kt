import misc.extensions.initialized
import misc.extensions.purposefulCreep
import misc.extensions.purposefulSpawn
import purposes.PurposefulConcept
import purposes.creeps.Hauler
import purposes.creeps.Miner
import purposes.creeps.PurposefulCreep
import purposes.structures.spawns.PurposefulSpawn
import screeps.api.*
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete

object Main {
    val roles = arrayOf(Hauler.CreepRoleCompanion, Miner.CreepRoleCompanion, PurposefulSpawn.SpawnRoleCompanion)

    // Used to detect when script is reloaded
    var initialized = false
}

val purposefulCreeps: List<PurposefulCreep>
    get() = Game.creeps.values.map { it.purposefulCreep }
val purposefulSpawns: List<PurposefulSpawn>
    get() = Game.spawns.values.map { it.purposefulSpawn }
val purposefulConcepts: List<PurposefulConcept>
    get() = purposefulCreeps + purposefulSpawns + Main.roles

@Suppress("unused")
fun loop() {
    if (!Main.initialized) {
        onReload()
        Main.initialized = true
    }

    if (!Memory.initialized) {
        init()
        Memory.initialized = true
    }

    gameLoop()
}

fun init() {}

fun gameLoop() {
    cleanMemory()

    handleMessages()

    executePurposes()

    if (Game.time % 5 == 0) {
        console.log(PurposefulSpawn.mailBox.toString())
    }
}

private fun cleanMemory() {
    if (Game.creeps.isEmpty()) return

    for ((creepName, _) in Memory.creeps) {
        if (Game.creeps[creepName] == null) {
            delete(Memory.creeps[creepName])
        }
    }
}

private fun handleMessages() {
    for (role in Main.roles) {
        role.handleMessages()
    }
}

private fun onReload() {
    for (purposefulBeing in purposefulConcepts) {
        purposefulBeing.onReload()
    }
}

private fun executePurposes() {
    for (purposefulBeing in purposefulConcepts) {
        purposefulBeing.execute()
    }
}