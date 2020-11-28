package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.errors.PenguBankAPIException
import club.pengubank.mobile.services.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginScreenState(private val loginService: LoginService) {

    var state: LoginState by mutableStateOf(LoginState.Empty)
    var email: String by mutableStateOf("a@b.c")
    var password: String by mutableStateOf("password")

    fun login() = GlobalScope.launch(Dispatchers.Main) {
        state = LoginState.Loading
        state = try {
            loginService.login(email, password)
            LoginState.Success
        } catch (t: PenguBankAPIException) {
            LoginState.Error(t.message)
        }
    }


    sealed class LoginState {
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
        object Loading : LoginState()
        object Empty : LoginState()
    }
}