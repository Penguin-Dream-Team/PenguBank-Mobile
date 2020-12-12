package club.pengubank.mobile.bluetooth.models.messages

import club.pengubank.mobile.bluetooth.models.JSONObject

data class PendingTransaction(
    val id: Int,
    val token: String,
    val amount: Int,
    val account: Int,
    val destination: Int,
    val createdAt: String,
    val expiredAt: String
)
