package storage

interface Storage<K, V> {
    val tableName: String

    fun get(key: K): V?

    fun set(value: V, key: K = generateKey(value)): K

    fun containsKey(key: K): Boolean

    fun generateKey(value: V): K

    fun remove(key: K)

    fun getAll(): Collection<V>

    fun toMap(): Map<K, V>
}