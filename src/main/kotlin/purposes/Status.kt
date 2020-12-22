package purposes

enum class Status {
    Created, //Still not initialized
    Idle, //Is not doing anything, but could start doing something on its own
    Sleeping, //Is not doing anything, will only wake up when receiving a message
    Active
}