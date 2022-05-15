package storage

class StorageHolder {
    companion object Factory {
        val notices = mapOf<String, NoticeStorage<*>>()
        val tickets = TicketStorage()
    }
}