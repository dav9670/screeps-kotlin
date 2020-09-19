import creeps.purposefulCreep
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import memory.initialized
import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete
import structures.spawns.spawn

object Main {
    val roles = arrayOf(Hauler.RoleCompanion, Miner.RoleCompanion)

    // Used to detect when script is reloaded
    var initialized = false
}

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

fun onReload() {
    for (role in Main.roles) {
        role.onReload()
    }
}

fun init() {}

fun gameLoop() {
    cleanMemory()

    spawn()

    handleMessages()

    executeCreeps()
}

private fun cleanMemory() {
    if (Game.creeps.isEmpty()) return

    for ((creepName, _) in Memory.creeps) {
        if (Game.creeps[creepName] == null) {
            delete(Memory.creeps[creepName])
        }
    }
}

private fun spawn() {
    val mainSpawn: StructureSpawn = Game.spawns.values.firstOrNull() ?: return

    val behavior = Main.roles.maxByOrNull { it.spawnBehavior.spawnPriority }!!.spawnBehavior

    if (behavior.spawnPriority > 0.0) {
        mainSpawn.spawn(behavior)
    }
}

private fun handleMessages() {
    for (role in Main.roles) {
        role.handleMessages()
    }
}

private fun executeCreeps() {
    for ((_, creep) in Game.creeps) {
        val purposefulCreep = creep.purposefulCreep

        purposefulCreep.execute()
    }
}