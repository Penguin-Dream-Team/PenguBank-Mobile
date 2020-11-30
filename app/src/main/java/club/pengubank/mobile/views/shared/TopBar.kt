package club.pengubank.mobile.views.shared

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.states.StoreState

@Composable
fun TopBar(navController: NavHostController, store: StoreState) =
    TopAppBar(title = { Text("PenguBank") }, actions = {
        if (store.loggedIn) {
            IconButton(
                onClick = {
                    store.logout()
                    navController.navigate("login")
                },
                icon = Icons.Outlined.ExitToApp,
                description = "Logout",
                selected = false
            )
        }
    })