package purposes

import screeps.api.StoreOwner

interface PurposefulBeing : PurposefulConcept, StoreOwner {
    val gameObject: StoreOwner
}