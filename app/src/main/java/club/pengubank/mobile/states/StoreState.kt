package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.storage.UserDataService
import kotlinx.coroutines.runBlocking

class StoreState(private val userDataService: UserDataService) {
    var token by mutableStateOf("")
    var loggedIn by mutableStateOf(false)

    fun logout() {
        token = ""
        loggedIn = false
    }

    var email: String =
        runBlocking { userDataService.getUserData().email }

    val enabled2FA: Boolean =
        runBlocking { userDataService.getUserData().totpKey.isNullOrBlank().not() }

    val hasPerformedSetup: Boolean =
        runBlocking { userDataService.getUserData().passcode.isNullOrBlank().not() }
}