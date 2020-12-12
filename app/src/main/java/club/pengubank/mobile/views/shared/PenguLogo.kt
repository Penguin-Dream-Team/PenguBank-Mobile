package club.pengubank.mobile.views.shared

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.R

@Composable
fun PenguLogo(modifier: Modifier = Modifier) {
    Image(
        asset = imageResource(id = R.drawable.logo),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .preferredHeight(128.dp)
            .preferredWidth(128.dp)
            .clip(shape = CircleShape),
    )
}

@Composable
fun PenguLogoWithTitle(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        PenguLogo()
        Spacer(modifier = Modifier.preferredHeight(12.dp))
        Text(
            text = "PenguBank",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
