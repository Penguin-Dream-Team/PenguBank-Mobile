package club.pengubank.mobile.views.dashboard.partials.qrcode

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.data.QueuedTransactions
import club.pengubank.mobile.services.TransactionService
import club.pengubank.mobile.states.StoreState

@Composable
fun QueuedTransactions(transactionService: TransactionService, storeState: StoreState) {

    val queuedTransactions: List<QueuedTransactions> = listOf(QueuedTransactions(
        id = 1,
        destinationId = 2,
        amount = 1000
    )) + listOf(QueuedTransactions(
        id = 2,
        destinationId = 2,
        amount = 1000
    )) + listOf(QueuedTransactions(
        id = 3,
        destinationId = 2,
        amount = 1000
    ))

    LazyColumnFor(queuedTransactions) { transaction ->
        if (queuedTransactions.indexOf(transaction) == 0) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "All Queued Transactions - ${storeState.bluetoothMac} - ${storeState.kPub}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Row() {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Id: " + transaction.id.toString(),
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "To: " + transaction.destinationId.toString(),
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Amount: " + transaction.amount.toString(),
            )

            IconButton(
                onClick = { transactionService.updateTransaction("accept", transaction)},
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.Top)
            ) {
                Icon(
                    asset = Icons.Filled.CheckCircle
                )
            }

            IconButton(
                onClick = { transactionService.updateTransaction("cancel", transaction)},
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.Top)
            ) {
                Icon(
                    asset = Icons.Filled.Cancel
                )
            }
        }
        Divider(color = Color(0x1100000))
    }
}
