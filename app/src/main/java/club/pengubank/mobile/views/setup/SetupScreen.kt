package club.pengubank.mobile.views.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.states.setup.SetupInitScreenState
import club.pengubank.mobile.states.setup.SetupPasscodeScreenState
import club.pengubank.mobile.states.setup.SetupScreenState
import club.pengubank.mobile.views.setup.components.SetupLoginForm
import club.pengubank.mobile.views.setup.components.SetupPasscodeForm
import club.pengubank.mobile.views.shared.PenguLogoWithTitle
import io.ktor.util.*

@KtorExperimentalAPI
@Composable
fun SetupScreen(navController: NavHostController, setupService: SetupService) {
    var screenState: SetupScreenState by remember { mutableStateOf(SetupInitScreenState(setupService)) }

    val setupNavController = rememberNavController()

    if (screenState.state.value is SetupScreenState.SetupState.InitState.Success) {
        screenState = SetupPasscodeScreenState(screenState.email, setupService)
        setupNavController.navigate("setup-passcode")
    }

    if (screenState.state.value is SetupScreenState.SetupState.PasscodeState.Success)
        navController.navigate("login")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        PenguLogoWithTitle(modifier = Modifier.padding(top = 64.dp, bottom = 24.dp))

        Text(
            text = "Setup - ${
                if (screenState is SetupInitScreenState)
                    "Enter credentials"
                else
                    "Choose passcode"
            }",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.preferredHeight(24.dp))

        NavHost(navController = setupNavController, startDestination = "setup-login") {
            composable("setup-login") {
                if (screenState !is SetupInitScreenState)
                    screenState = SetupInitScreenState(setupService)

                SetupLoginForm(screenState as SetupInitScreenState)
            }

            composable("setup-passcode") {
                SetupPasscodeForm(screenState as SetupPasscodeScreenState)
            }
        }
    }
}


