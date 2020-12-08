package club.pengubank.mobile.bluetooth.models

import club.pengubank.mobile.bluetooth.models.messages.PendingTransaction
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

inline fun <reified T : JSONObject> String.toObject(): T = Gson().fromJson(this, T::class.java)

interface JSONObject {
    fun toJSON(): String = Gson().toJson(this)
}

sealed class Response : JSONObject {

    data class RetrievePendingTransactionsResponse(
        val pendingTransactions: List<PendingTransaction>
    ) : Response()

    data class UpdatePendingTransactionResponse(val ok: Boolean) : Response()

    data class ErrorResponse(val message: String?) : Response()
}
