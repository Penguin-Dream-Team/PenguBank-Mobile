package club.pengubank.mobile.states.setup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

abstract class SetupScreenState(initialState: SetupState) {
    var state: MutableState<SetupState> = mutableStateOf(initialState)

    open var email: MutableState<String> = mutableStateOf("")

    sealed class SetupState {
        sealed class InitState : SetupState() {
            object Success : InitState()
            data class Error(val message: String) : InitState()
            object Loading : InitState()
            object Empty : InitState()
        }

        sealed class PasscodeState : SetupState() {
            object Success : PasscodeState()
            data class Error(val message: String) : PasscodeState()
            object Loading : PasscodeState()
            object Empty : PasscodeState()
        }
    }
}
