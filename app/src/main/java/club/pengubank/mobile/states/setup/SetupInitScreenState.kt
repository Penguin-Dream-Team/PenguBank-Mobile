package club.pengubank.mobile.states.setup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.errors.PenguBankAPIException
import club.pengubank.mobile.services.SetupService
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@KtorExperimentalAPI
class SetupInitScreenState(private val setupService: SetupService) :
    SetupScreenState(SetupState.InitState.Empty) {

    var password: MutableState<String> = mutableStateOf("")

    fun login() = GlobalScope.launch(Dispatchers.Main) {
        state.value = SetupState.InitState.Loading
        state.value = try {
            setupService.login(email.value, password.value)
            SetupState.InitState.Success
        } catch (t: PenguBankAPIException) {
            SetupState.InitState.Error(t.message)
        }
    }
}