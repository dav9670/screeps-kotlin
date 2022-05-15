package purposes

import notices.Receiver
import notices.Sender

interface PurposefulConcept : Sender, Receiver {
    fun init()
    fun execute()
    fun onReload()
}