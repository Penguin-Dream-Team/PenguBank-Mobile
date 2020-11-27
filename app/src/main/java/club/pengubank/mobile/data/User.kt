package club.pengubank.mobile.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("registeredAt") val registeredAt: String,
    @SerializedName("enabled2FA") val enabled2FA: Boolean,
    @SerializedName("accountId") val accountId: Int
)
