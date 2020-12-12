package club.pengubank.mobile.views.dashboard.partials

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.Toasts
import club.pengubank.mobile.views.dashboard.partials.qrcode.QRCodeScan
import club.pengubank.mobile.views.dashboard.partials.qrcode.QueuedTransactions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun TransactionSection(
    navController: NavController,
    store: StoreState,
    requestBluetooth: () -> Unit,
    context: Context
) {
    val loading = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

        if (store.qrcodeScanned) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Pending Transactions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                IconButton(
                    onClick = {
                        if (!loading.value) {
                            GlobalScope.launch {
                                loading.value = true
                                val handler = Handler(Looper.getMainLooper())
                                try {
                                    store.client?.refreshPendingTransactions()
                                    handler.post {
                                        Toasts.notifyUser(context, "Refreshed pending transactions")
                                    }
                                } catch (e: Exception) {
                                    handler.post {
                                        Toasts.notifyUser(context, e.message!!)
                                    }
                                }
                                loading.value = false
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.Top)
                ) {
                    Icon(
                        asset = Icons.Outlined.Refresh
                    )
                }
            }

            Divider(color = Color.DarkGray.copy(alpha = 0.3f))

            QueuedTransactions(store, loading, context)
        } else {
            Text(
                "Connect to Desktop App",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            QRCodeScan(navController, store, requestBluetooth)
        }
    }
}
