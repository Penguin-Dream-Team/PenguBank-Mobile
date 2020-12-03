package club.pengubank.mobile.data

import com.google.gson.annotations.SerializedName

data class QueuedTransactions(
    @SerializedName("id") var id: Int,
    @SerializedName("destinationId") var destinationId: Int,
    @SerializedName("amount") var amount: Int,
)
