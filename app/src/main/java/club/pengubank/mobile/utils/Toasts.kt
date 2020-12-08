package club.pengubank.mobile.utils

import android.content.Context
import android.widget.Toast

object Toasts {
    fun notifyUser(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}