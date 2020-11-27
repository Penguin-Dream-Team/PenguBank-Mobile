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

    var state: LoginUIState by mutableStateOf(LoginUIState.Empty)
    var email: String by mutableStateOf("a@b.c")
    var password: String by mutableStateOf("password")

    fun login() = GlobalScope.launch(Dispatchers.Main) {
        state = LoginUIState.Loading
        state = try {
            email = loginService.login(email, password)
            LoginUIState.Success
        } catch (t: PenguBankAPIException) {
            LoginUIState.Error(t.message)
        }
    }


    sealed class LoginUIState {
        object Success : LoginUIState()
        data class Error(val message: String) : LoginUIState()
        object Loading : LoginUIState()
        object Empty : LoginUIState()
    }
}