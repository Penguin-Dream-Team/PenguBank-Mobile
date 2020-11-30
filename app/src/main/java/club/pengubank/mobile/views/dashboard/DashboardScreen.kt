package club.pengubank.mobile.views.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo
import club.pengubank.mobile.views.dashboard.partials.totp.TOTPSection

@InternalInteropApi
@Composable
fun DashboardScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }

    Column {
        UserInfo(storeState)
        TOTPSection(navController, storeState)
        TransactionSection(navController, storeState)
    }
}
