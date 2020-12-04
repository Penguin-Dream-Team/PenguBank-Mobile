package club.pengubank.mobile.api

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import club.pengubank.mobile.api.models.Response
import club.pengubank.mobile.data.User
import club.pengubank.mobile.api.requests.LoginRequest
import club.pengubank.mobile.api.requests.SetupRequest
import club.pengubank.mobile.errors.PenguBankAPIException
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.utils.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import java.util.concurrent.TimeoutException

@KtorExperimentalAPI
class PenguBankApi(
    private val context: Context,
    private val store: StoreState
) {

    companion object {
        private val APIConfig = Config.load()
        val PROTOCOL = APIConfig.protocol
        val HOST = APIConfig.host
        val TIMEOUT = APIConfig.timeout
    }

    private val api = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            host = HOST
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
    }

    suspend fun login(loginRequest: LoginRequest): Response.SuccessResponse<User> =
        post(Routes.LOGIN, loginRequest)

    @SuppressLint("DefaultLocale", "HardwareIds")
    suspend fun setup(setupRequest: SetupRequest): Response.SuccessResponse<User> {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val info = manager.adapter
        setupRequest.phoneMACAddress = info.address.toUpperCase()
        return post(Routes.SETUP, setupRequest)
    }

    suspend fun dashboard(): Response.SuccessResponse<Any> = get("/dashboard")

    private suspend inline fun <reified T> get(path: String = "/"): T =
        try {
            api.get(scheme = PROTOCOL, path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    private suspend inline fun <reified T> post(path: String, data: Any): T =
        try {
            api.post(scheme = PROTOCOL, path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
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
        if (store.token.isNotBlank()) headers["Authorization"] = "Bearer ${store.token}"
    }
}