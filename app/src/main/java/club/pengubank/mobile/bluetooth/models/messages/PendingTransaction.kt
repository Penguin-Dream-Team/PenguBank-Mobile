package club.pengubank.mobile.bluetooth.models.messages

data class PendingTransaction(
    val id: Int,
    val token: String,
    val amount: Int,
    val account: String,
    val destination: String,
    val createdAt: String,
    val expiredAt: String
)
