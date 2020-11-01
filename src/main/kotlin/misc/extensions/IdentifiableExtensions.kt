package misc.extensions

import screeps.api.Creep
import screeps.api.Game
import screeps.api.Identifiable
import screeps.api.values

fun Identifiable.creepsTargeting(baseFilter: List<Creep> = Game.creeps.values.toList()): Array<Creep> {
    return baseFilter.filter { it.memory.targetId == id }.toTypedArray()
}