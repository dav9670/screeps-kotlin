package misc.extensions

import screeps.api.FIND_SOURCES
import screeps.api.Game
import screeps.api.Memory
import screeps.api.values
import screeps.utils.memory.memory

var Memory.initialized by memory { false }
var Memory.sourceSpots by memory {
    val mainRoomSources = Game.rooms.values[0].find(FIND_SOURCES)
    mainRoomSources.sumBy { it.noWallSpots() }
}