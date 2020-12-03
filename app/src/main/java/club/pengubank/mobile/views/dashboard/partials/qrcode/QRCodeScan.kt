package club.pengubank.mobile.views.dashboard.partials.qrcode

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.states.StoreState

@Composable
fun QRCodeScan(storeState: StoreState) {

    Row(
        modifier = Modifier
            .preferredHeight(56.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { storeState.qrcodeScanned = true },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight()
        ) {
            Text(
                text = "Scan QR Code",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}
