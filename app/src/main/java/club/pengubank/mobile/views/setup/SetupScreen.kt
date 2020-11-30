package club.pengubank.mobile.views.setup

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.states.setup.SetupInitScreenState
import club.pengubank.mobile.states.setup.SetupPasscodeScreenState
import club.pengubank.mobile.states.setup.SetupScreenState
import club.pengubank.mobile.views.setup.components.SetupLoginForm
import club.pengubank.mobile.views.setup.components.SetupPasscodeForm
import io.ktor.util.*

@KtorExperimentalAPI
@Composable
fun SetupScreen(navController: NavController, setupService: SetupService) {
    var screenState: SetupScreenState by remember { mutableStateOf(SetupInitScreenState(setupService)) }

    if (screenState.state.value is SetupScreenState.SetupState.InitState.Success)
        screenState = SetupPasscodeScreenState(screenState.email, setupService)

    if (screenState.state.value is SetupScreenState.SetupState.PasscodeState.Success)
        navController.navigate("login")

    when(screenState) {
        is SetupInitScreenState -> SetupLoginForm(screenState as SetupInitScreenState)
        is SetupPasscodeScreenState -> SetupPasscodeForm(screenState as SetupPasscodeScreenState)
    }
}
