package club.pengubank.mobile.services

import club.pengubank.mobile.security.BCrypt
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService

class LoginService(
    private val userDataService: UserDataService,
    private val store: StoreState
) {

    suspend fun login(passcode: String): Boolean {
        val result = BCrypt.checkpw(passcode, userDataService.getUserData().passcode)
        store.loggedIn = result
        return result
    }

    val email: String get() = store.email

    fun logout() = store.logout()
    fun loginBiometric() {
        store.loggedIn = true
    }
}