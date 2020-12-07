package club.pengubank.mobile.states.setup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import club.pengubank.mobile.errors.PenguBankAPIException
import club.pengubank.mobile.services.SetupService
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@KtorExperimentalAPI
class SetupPasscodeScreenState(
    override var email: MutableState<String>,
    private val setupService: SetupService
) : SetupScreenState(SetupState.PasscodeState.Empty) {

    var passcode: MutableState<String> = mutableStateOf("passcode123")
    var passcodeConfirm: MutableState<String> = mutableStateOf("passcode123")

    fun registerPasscode() = GlobalScope.launch(Dispatchers.Main) {
        state.value = SetupState.PasscodeState.Loading
        state.value = if (passcode.value != passcodeConfirm.value) SetupState.PasscodeState.Error("Passcodes do not match.")
        else try {
            setupService.registerPasscode(email.value, passcode.value)
            SetupState.PasscodeState.Success
        } catch (t: PenguBankAPIException) {
            SetupState.PasscodeState.Error(t.message)
        }
    }
}