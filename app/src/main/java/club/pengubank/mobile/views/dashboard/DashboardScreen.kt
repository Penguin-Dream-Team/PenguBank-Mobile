package club.pengubank.mobile.views.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.totp.TOTPAuthenticator
import club.pengubank.mobile.utils.totp.TOTPConfig
import club.pengubank.mobile.views.dashboard.partials.totp.TOTPSection
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo
import java.time.Instant
import java.util.*

@Composable
fun DashboardScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }

    Column {
        UserInfo(storeState)
        TOTPSection(navController, storeState)
        TransactionSection(navController, storeState)
    }
}
