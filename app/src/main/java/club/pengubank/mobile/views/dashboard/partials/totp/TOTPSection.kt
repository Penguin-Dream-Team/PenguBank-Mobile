package club.pengubank.mobile.views.dashboard.partials.totp

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.InternalInteropApi
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState

@InternalInteropApi
@Composable
fun TOTPSection(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val enabled2FA by remember { mutableStateOf(store.enabled2FA) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Two Factor Authentication",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        if (enabled2FA)
            TOTPCodeBar(storeState)
        else
            TOTPSetupBar(navController, storeState)

        Divider(modifier = Modifier.padding(top = 6.dp))
    }
}

