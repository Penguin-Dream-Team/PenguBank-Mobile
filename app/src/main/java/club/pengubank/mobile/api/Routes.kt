package club.pengubank.mobile.api

import com.google.gson.annotations.SerializedName

sealed class Response<out T> {
    data class SuccessResponse<T>(
        @SerializedName("token") val token: String,
        @SerializedName("data") val data: T
    ) : Response<T>()

    data class ErrorResponse(val message: String) : Response<Nothing>()
}

const val LOGIN_ROUTE = "/login"