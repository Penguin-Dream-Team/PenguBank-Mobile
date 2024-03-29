package club.pengubank.mobile.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.services.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginScreenState(private val loginService: LoginService) {

    var state: LoginState by mutableStateOf(LoginState.Empty)

    val email: String = loginService.email
    var passcode: MutableState<String> = mutableStateOf("")

    var fingerprintAttempts by mutableStateOf(0)

    fun login() = GlobalScope.launch(Dispatchers.Main) {
        state = LoginState.Loading
        state = if (loginService.login(passcode.value)) {
            fingerprintAttempts = 0
            LoginState.Success
        }
        else
            LoginState.Error("The passcode provided does not match the stored records")
    }

    fun biometricLogin() = GlobalScope.launch(Dispatchers.Main) {
        loginService.loginBiometric()
        state = LoginState.Success
        fingerprintAttempts = 0
    }

    sealed class LoginState {
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
        object Loading : LoginState()
        object Empty : LoginState()
    }
}