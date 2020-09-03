package creeps.purposefulCreeps.roles

import creeps.purposefulCreeps.PurposefulCreep
import screeps.api.Identifiable

interface Message<S: Identifiable,in R: Identifiable> {
    val sender: S

    fun affinity(receiver: R): Double
}