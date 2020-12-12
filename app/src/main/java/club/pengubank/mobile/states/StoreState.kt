package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.bluetooth.Client
import club.pengubank.mobile.bluetooth.models.messages.PendingTransaction
import club.pengubank.mobile.storage.UserDataService
import kotlinx.coroutines.runBlocking

class StoreState(private val userDataService: UserDataService) {
    var token by mutableStateOf("")
    var loggedIn by mutableStateOf(false)
    var operation by mutableStateOf("")
    var qrcodeScanned by mutableStateOf(false)
    var bluetoothMac by mutableStateOf("")
    var kPub by mutableStateOf("")
    var client: Client? by mutableStateOf(null)
    var queuedTransactions: List<PendingTransaction> by mutableStateOf(emptyList())

    fun logout() {
        token = ""
        loggedIn = false
        operation = ""
        qrcodeScanned = false
        bluetoothMac = ""
        kPub = ""
        client?.close()
        client = null
    }

    var email: String = ""
        get() = runBlocking { userDataService.getUserData().email }

    var enabled2FA: Boolean = runBlocking { userDataService.getUserData().totpKey.isNullOrBlank().not() }

    val hasPerformedSetup: Boolean
        get() = runBlocking { userDataService.getUserData().passcode.isNullOrBlank().not() }

    var totpSecretKey: String = runBlocking { userDataService.getUserData().totpKey }
}