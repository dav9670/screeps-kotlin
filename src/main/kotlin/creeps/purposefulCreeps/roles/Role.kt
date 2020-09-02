package creeps.purposefulCreeps.roles

import creeps.purposefulCreeps.PurposefulCreep
import creeps.role
import screeps.api.Creep
import screeps.api.Game
import screeps.api.values

open class Role<out T: PurposefulCreep>(val spawnBehavior: SpawnBehavior) {
    val creeps: Array<Creep>
        get() {
            return Game.creeps.values.filter { it.memory.role == spawnBehavior.role }.toTypedArray()
        }

    val purposefulCreeps: Array<out T>
        get() {
            return creeps.map { it.unsafeCast<T>() }.toTypedArray()
        }

    val roleCount: Int
        get() {
            return creeps.size
        }
}