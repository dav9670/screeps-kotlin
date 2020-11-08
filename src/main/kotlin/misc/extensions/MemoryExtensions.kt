package misc.extensions

import screeps.api.FIND_SOURCES
import screeps.api.Game
import screeps.api.Memory
import screeps.api.values
import screeps.utils.memory.memory

var Memory.initialized by memory { false }
var Memory.sourceSpots by memory { getSourceSpots() }

var Memory.mainSpawnId by memory { Game.spawns.values.first().id }

fun Memory.onRespawn() {
    mainSpawnId = Game.spawns.values.first().id
    sourceSpots = getSourceSpots()
}

fun getSourceSpots(): Int {
    val mainRoomSources = Game.rooms.values[0].find(FIND_SOURCES)
    return mainRoomSources.sumBy { it.noWallSpots() }
}