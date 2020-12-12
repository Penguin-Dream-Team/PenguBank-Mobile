package club.pengubank.mobile.views.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.states.StoreState

@Composable
fun TopBar(navController: NavHostController, store: StoreState) =
    if (store.loggedIn) {
        TopAppBar(title = { Text("PenguBank") }, actions = {
            IconButton(
                onClick = {
                    store.logout()
                    navController.navigate("login")
                },
                icon = Icons.Outlined.ExitToApp,
                description = "Logout",
                selected = false
            )
        }, navigationIcon = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PenguLogo(
                    modifier = Modifier
                        .preferredWidth(36.dp)
                        .preferredHeight(36.dp)
                )
            }
        })
    } else Unit
