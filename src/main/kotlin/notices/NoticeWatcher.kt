package notices

class NoticeWatcher(private val board: Board<*>, private val key: String) {
    fun noticeEmpty() {
        board.remove(key)
    }
}