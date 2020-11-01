package storage


abstract class MemoryStorage<K, V> : Storage<K, V> {
    protected val map: MutableMap<K, V> = mutableMapOf()

    final override fun get(key: K): V? {
        return map[key]
    }

    override fun set(value: V): K {
        val key = generateKey()
        map[key] = value
        return key
    }

    override fun remove(key: K) {
        map.remove(key)
    }

    protected abstract fun generateKey(): K
}