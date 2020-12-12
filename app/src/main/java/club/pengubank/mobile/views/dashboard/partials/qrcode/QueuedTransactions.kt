package club.pengubank.mobile.views.dashboard.partials.qrcode

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.data.QueuedTransaction
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.Toasts
import club.pengubank.mobile.utils.biometric.BiometricUtils
import club.pengubank.mobile.utils.toEuros
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun QueuedTransactions(store: StoreState, loading: MutableState<Boolean>, context: Context) {
    val queuedTransactions = store.queuedTransactions

    if (queuedTransactions.isEmpty())
        Text(
            text = "No pending transactions",
            modifier = Modifier
                .padding(vertical = 10.dp)
        )

    LazyColumnFor(
        queuedTransactions,
        modifier = Modifier.fillMaxWidth()
    ) { transaction ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "To: ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = transaction.destination,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Amount: ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = transaction.amount.toEuros(),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row {
                IconButton(
                    onClick = {
                        if (!loading.value) {
                            GlobalScope.launch {
                                loading.value = true
                                val handler = Handler(Looper.getMainLooper())
                                try {
                                    store.client?.approvePendingTransactions(
                                        transaction.id,
                                        transaction.token
                                    )
                                    handler.post {
                                        Toasts.notifyUser(context, "Approved transaction")
                                    }
                                    store.client?.refreshPendingTransactions()
                                } catch (e: Exception) {
                                    handler.post {
                                        Toasts.notifyUser(context, e.message!!)
                                    }
                                }
                                loading.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        asset = Icons.Filled.CheckCircle,
                        tint = Color(52, 247, 133)
                    )
                }

                IconButton(
                    onClick = {
                        if (!loading.value) {
                            GlobalScope.launch {
                                loading.value = true
                                val handler = Handler(Looper.getMainLooper())
                                try {
                                    store.client?.cancelPendingTransactions(
                                        transaction.id,
                                        transaction.token
                                    )
                                    handler.post {
                                        Toasts.notifyUser(context, "Canceled transaction")
                                    }
                                    store.client?.refreshPendingTransactions()
                                } catch (e: Exception) {
                                    handler.post {
                                        Toasts.notifyUser(context, e.message!!)
                                    }
                                }
                                loading.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        asset = Icons.Filled.Cancel,
                        tint = Color(247, 72, 52)
                    )
                }
            }
        }

        if (queuedTransactions.last().id != transaction.id)
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
    }
}

