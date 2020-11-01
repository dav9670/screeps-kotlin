package storage

interface Storage<K, V> {
    val tableName: String

    fun get(key: K): V?

    fun set(value: V): K

    fun remove(key: K)
}