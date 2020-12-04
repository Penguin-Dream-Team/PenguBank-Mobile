package club.pengubank.mobile.views.shared

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.semantics.accessibilityLabel
import androidx.compose.ui.semantics.semantics

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: VectorAsset,
    description: String,
    selected: Boolean
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .semantics { accessibilityLabel = description }
            .fillMaxHeight()
            .wrapContentHeight()
    ) {
        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
            val tint = if (selected) MaterialTheme.colors.primary else AmbientContentColor.current
            Icon(
                icon,
                tint = tint,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}