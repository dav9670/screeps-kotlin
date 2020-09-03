package structures.sources

import creeps.purposefulCreeps.roles.miner.Miner
import creepsTargeting
import screeps.api.Creep
import screeps.api.Identifiable
import screeps.api.Source
import screeps.api.TERRAIN_MASK_WALL
import screeps.utils.copy

fun Source.noWallSpots(): Int {
    var spots = 0
    val upLeft = pos.copy(pos.x - 1, pos.y - 1)

    for (x in 0 until 3) {
        for (y in 0 until 3) {
            val terrainType = room.getTerrain().get(upLeft.x + x, upLeft.y + y)
            if (terrainType != TERRAIN_MASK_WALL) {
                spots++
            }
        }
    }

    return spots
}

fun Source.creepsTargeting(): Array<Creep> {
    val miners = Miner.creeps
    return (this.unsafeCast<Identifiable>()).creepsTargeting(miners)
}

fun Source.availableSpots(): Int {
    return noWallSpots() - creepsTargeting().size
}