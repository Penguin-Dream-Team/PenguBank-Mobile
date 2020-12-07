package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.storage.UserDataService
import kotlinx.coroutines.runBlocking

class StoreState(private val userDataService: UserDataService) {
    var token by mutableStateOf("")
    var loggedIn by mutableStateOf(false)
    var operation by mutableStateOf("")
    var qrcodeScanned by mutableStateOf(false)
    var bluetoothMac by mutableStateOf("")
    var kPub by mutableStateOf("")

    fun logout() {
        token = ""
        loggedIn = false
        operation = ""
        qrcodeScanned = false
        bluetoothMac = ""
        kPub = ""
    }

    var email: String = ""
        get() = runBlocking { userDataService.getUserData().email }

    val enabled2FA: Boolean
        get() = runBlocking { userDataService.getUserData().totpKey.isNullOrBlank().not() }

    val hasPerformedSetup: Boolean
        get() = runBlocking { userDataService.getUserData().passcode.isNullOrBlank().not() }
}