package club.pengubank.mobile.services

import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.data.requests.LoginRequest
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService
import io.ktor.util.*

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
        userDataService.storeUserData(email = email, passcode = passcode)
    }
}