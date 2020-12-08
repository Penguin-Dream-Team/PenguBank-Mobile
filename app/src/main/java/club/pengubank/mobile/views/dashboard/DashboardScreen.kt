package club.pengubank.mobile.views.dashboard

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.dashboard.partials.TransactionSection
import club.pengubank.mobile.views.dashboard.partials.UserInfo
import club.pengubank.mobile.views.dashboard.partials.totp.TOTPSection

@InternalInteropApi
@Composable
fun DashboardScreen(navController: NavController, store: StoreState, requestBluetooth: () -> Unit, context: Context) {
    val storeState by remember { mutableStateOf(store) }

    Column {
        UserInfo(storeState)
        TOTPSection(navController, storeState)
        Spacer(modifier = Modifier.padding(10.dp))
        TransactionSection(navController, storeState, requestBluetooth, context)
    }
}
