package club.pengubank.mobile.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import club.pengubank.mobile.R

private val MontserratFontFamily = fontFamily(
    font(R.font.montserrat_regular),
    font(R.font.montserrat_light, FontWeight.Light),
    font(R.font.montserrat_medium, FontWeight.Medium),
    font(R.font.montserrat_semibold, FontWeight.SemiBold)
)

@Composable
fun PenguBankTheme(content: @Composable () -> Unit) {
    val colors = darkColors(
        primary = Green500,
        surface = DarkBlue900,
        onSurface = Color.White,
        background = DarkBlue900,
        onBackground = Color.White,
        onPrimary = DarkBlue900,
        onSecondary = Color.LightGray
    )

    val typography = Typography(
        defaultFontFamily = MontserratFontFamily,
        h1 = TextStyle(
            fontWeight = FontWeight.W100,
            fontSize = 96.sp,
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 44.sp,
            fontFamily = MontserratFontFamily,
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 14.sp
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 34.sp
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 24.sp
        ),
        h6 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            fontFamily = MontserratFontFamily,
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        subtitle2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        body2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        button = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
        ),
        caption = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 12.sp
        ),
        overline = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 10.sp
        )
    )
    MaterialTheme(colors = colors, typography = typography, content = content)
}
