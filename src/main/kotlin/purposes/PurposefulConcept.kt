package purposes

import messages.Receiver
import messages.Sender

interface PurposefulConcept : Sender, Receiver {
    fun init()
    fun execute()
    fun onReload()
}