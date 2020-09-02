import creeps.targetId
import screeps.api.Creep
import screeps.api.Game
import screeps.api.Identifiable
import screeps.api.values

fun Identifiable.creepsTargeting(baseFilter: Array<Creep> = Game.creeps.values): Array<Creep> {
    return baseFilter.filter { it.memory.targetId == id }.toTypedArray()
}