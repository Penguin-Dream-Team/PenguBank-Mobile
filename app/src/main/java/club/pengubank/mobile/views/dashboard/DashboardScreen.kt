package club.pengubank.mobile.views.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.dashboard.partials.totp.TOTPSection
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo

@Composable
fun DashboardScreen(navController: NavController, store: StoreState) {
    Column {
        UserInfo(store)
        TOTPSection(navController, store)
        TransactionSection(navController, store)
    }
}
