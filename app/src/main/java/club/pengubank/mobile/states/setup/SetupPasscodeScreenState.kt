package club.pengubank.mobile.states.setup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
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

    var passcode: MutableState<String> = mutableStateOf("")
    var passcodeConfirm: MutableState<String> = mutableStateOf("")

    fun registerPasscode() = GlobalScope.launch(Dispatchers.Main) {
        state.value = SetupState.PasscodeState.Loading
        state.value = when {
            passcode.value != passcodeConfirm.value -> SetupState.PasscodeState.Error("Passcodes do not match.")

            passcode.value.length < 6 -> SetupState.PasscodeState.Error("Passcode needs to be at least 6 digits")

            !passcode.value.isDigitsOnly() -> SetupState.PasscodeState.Error("Passcode can only contain digits")

            else -> try {
                setupService.registerPasscode(email.value, passcode.value)
                SetupState.PasscodeState.Success
            } catch (t: PenguBankAPIException) {
                SetupState.PasscodeState.Error(t.message)
            }
        }
    }
}