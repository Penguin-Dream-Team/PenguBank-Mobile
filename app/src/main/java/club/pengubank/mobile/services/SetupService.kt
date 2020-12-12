package club.pengubank.mobile.services

import android.util.Log
import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.api.requests.LoginRequest
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@KtorExperimentalAPI
class SetupService constructor(
    private val userDataService: UserDataService,
    private val store: StoreState,
    private val api: PenguBankApi
) {

    suspend fun login(email: String, password: String) {
        store.token = ""
        val response = api.login(LoginRequest(email, password))
        store.email = email
        store.token = response.token
    }

    suspend fun registerPasscode(email: String, passcode: String) {
        api.setup()
        userDataService.storeUserData(email = email, passcode = passcode)
    }

    fun registerTOTP(totp: String) =
        runBlocking {
            userDataService.storeUserData(totpKey = totp)
            store.totpSecretKey = totp
            store.enabled2FA = true
        }
}