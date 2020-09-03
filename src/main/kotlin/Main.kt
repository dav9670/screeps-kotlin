import creeps.purposefulCreep
import creeps.purposefulCreeps.roles.hauler.Hauler
import creeps.purposefulCreeps.roles.miner.Miner
import memory.initialized
import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete
import structures.spawns.spawn

/**
 * Entry point
 * is called by screeps
 *
 * must not be removed by DCE
 */
@Suppress("unused")
fun loop() {
    if (!Memory.initialized) {
        init()
        Memory.initialized = true
    }

    gameLoop()
}

fun init() {}

fun gameLoop() {
    cleanMemory()

    spawn()

    executeCreeps()
}

private fun cleanMemory() {
    if (Game.creeps.isEmpty()) return  // this is needed because Memory.creeps is undefined

    for ((creepName, _) in Memory.creeps) {
        if (Game.creeps[creepName] == null) {
            delete(Memory.creeps[creepName])
        }
    }
}

private fun spawn() {
    val mainSpawn: StructureSpawn = Game.spawns.values.firstOrNull() ?: return

    val behaviors = arrayOf(Hauler.spawnBehavior, Miner.spawnBehavior)

    val behavior = behaviors.maxBy { it.spawnPriority }!!

    if (behavior.spawnPriority > 0.0) {
        mainSpawn.spawn(behavior)
    }
}

private fun executeCreeps() {
    for ((_, creep) in Game.creeps) {
        val purposefulCreep = creep.purposefulCreep

        purposefulCreep.execute()
    }
}