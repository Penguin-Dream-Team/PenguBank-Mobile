package club.pengubank.mobile.api

import club.pengubank.mobile.data.User
import club.pengubank.mobile.data.requests.LoginRequest
import club.pengubank.mobile.errors.PenguBankAPIException
import club.pengubank.mobile.states.StoreState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import java.util.concurrent.TimeoutException

class PenguBankApi(
    private val store: StoreState
) {
    companion object {
        const val HOST = "192.168.1.43"
        const val PORT = 8080
        const val TIMEOUT_IN_SECONDS = 3
    }

    private val api = HttpClient {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            host = HOST
            port = PORT
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT_IN_SECONDS * 1000L
        }
    }

    suspend fun login(loginRequest: LoginRequest): Response.SuccessResponse<User> {
        return post(LOGIN_ROUTE, loginRequest)
    }

    suspend fun dashboard(): Response.SuccessResponse<Any> {
        return get("/dashboard")
    }

    private suspend inline fun <reified T> get(path: String = "/"): T {
        return try {
            api.get(path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) { handleApiException(e) as T }
    }

    private suspend inline fun <reified T> post(path: String, data: Any): T {
        return try {
            api.post(path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) { handleApiException(e) as T }
    }

    private suspend fun handleApiException(e: Exception) {
        val err = when (e) {
            is HttpRequestTimeoutException,
            is SocketTimeoutException,
            is java.net.SocketTimeoutException,
            is TimeoutException,
            is ConnectTimeoutException -> Response.ErrorResponse("Can't reach the server")
            is ClientRequestException -> e.response?.receive<Response.ErrorResponse>()
                ?: Response.ErrorResponse("Oops, something went wrong")
            else -> {
                Response.ErrorResponse(e.message ?: "Unknown error")
            }
        }

        throw PenguBankAPIException(err.message)
    }

    private fun addJWTTokenToRequest(headers: HeadersBuilder) {
        if (store.token.isNotBlank())
            headers["Authorization"] = "Bearer ${store.token}"
    }

}