package club.pengubank.mobile.services

import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.data.User
import club.pengubank.mobile.data.requests.LoginRequest
import club.pengubank.mobile.states.StoreState

class LoginService(
    private val store: StoreState,
    private val api: PenguBankApi
) {

    suspend fun login(email: String, password: String) {
        val response = api.login(LoginRequest(email, password))
        store.token = response.token
        store.user = response.data
        val newRes = api.dashboard()
        store.token = newRes.data.toString()
    }

}