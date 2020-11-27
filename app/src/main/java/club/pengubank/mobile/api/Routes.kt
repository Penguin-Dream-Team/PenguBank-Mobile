package club.pengubank.mobile.api

import com.fasterxml.jackson.annotation.JsonTypeInfo


data class LoginData(val email: String)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION
)
sealed class Response<out T> {
    data class SuccessResponse<T>(val token: String, val data: T) : Response<T>()
    data class ErrorResponse(val message: String) : Response<Nothing>()
}

const val LOGIN_ROUTE = "login"