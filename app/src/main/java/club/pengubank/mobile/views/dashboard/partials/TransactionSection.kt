package club.pengubank.mobile.views.dashboard.partials

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import club.pengubank.mobile.services.TransactionService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.dashboard.partials.qrcode.QRCodeScan
import club.pengubank.mobile.views.dashboard.partials.qrcode.QueuedTransactions

@Composable
fun TransactionSection(navController: NavController, transactionService: TransactionService, storeState: StoreState) {

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Connect to Desktop App",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        if(storeState.qrcodeScanned)
            QueuedTransactions(transactionService)
        else
            QRCodeScan(navController, storeState)

        Divider()
    }
}
