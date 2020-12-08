package club.pengubank.mobile.services

import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService

class LoginService(
    private val userDataService: UserDataService,
    private val store: StoreState
) {

    suspend fun login(passcode: String): Boolean {
        // TODO("USE BCRYPT AND DECRYPT")
        val result = userDataService.getUserData().passcode == passcode
        store.loggedIn = result
        return result
    }

    val email: String get() = store.email

    fun logout() = store.logout()
    fun loginBiometric() {
        // TODO decipher totp secret key
        store.loggedIn = true
    }
}