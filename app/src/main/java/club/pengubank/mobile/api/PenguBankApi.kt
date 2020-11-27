package club.pengubank.mobile.api

import android.util.Log
import club.pengubank.mobile.data.requests.LoginRequest
import club.pengubank.mobile.errors.PenguBankAPIException
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import java.lang.Exception
import java.util.concurrent.TimeoutException

class PenguBankApi {

    companion object {
        const val BASE_URL = "http://191.168.1.43:8080"
    }

    private val api = HttpClient {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
        }

/*
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
*/
    }

/*
    private suspend fun <T> POST(route: String, data: Any? = null): Response<T> {
        return try {
            api.post("$BASE_URL/$route") { if (data != null) body = data }
        } catch (e: Exception) {
            return Response.ErrorResponse("$BASE_URL/$route" ?: "Unknown error")
            when (e) {
                is HttpRequestTimeoutException,
                is SocketTimeoutException,
                is java.net.SocketTimeoutException,
                is TimeoutException,
                is ConnectTimeoutException -> Response.ErrorResponse("Can't reach the server")
                else -> {
                    //Response.ErrorResponse(e.message ?: "Unknown error")
                    Response.ErrorResponse("$BASE_URL/$route" ?: "Unknown error")
                }
            }
        }
    }

    private suspend fun <T> GET(route: String): Response<T> {
        return try {
            api.get("$BASE_URL/$route")
        } catch (e: Exception) {
            return Response.ErrorResponse("$BASE_URL/$route" ?: "Unknown error")
            when (e) {
                is HttpRequestTimeoutException,
                is SocketTimeoutException,
                is java.net.SocketTimeoutException,
                is TimeoutException,
                is ConnectTimeoutException -> Response.ErrorResponse("Can't reach the server")
                else -> {
                    //Response.ErrorResponse(e.message ?: "Unknown error")
                    Response.ErrorResponse("$BASE_URL/$route" ?: "Unknown error")
                }
            }
        }
    }

*/
    suspend fun login(loginRequest: LoginRequest): Response.SuccessResponse<LoginData> {

        val response = api.get<String>(BASE_URL)

        return Response.SuccessResponse(response, LoginData("a@b.c"))

/*
        return when (
            //val response = POST<LoginData>("login", loginRequest)
            val response = GET<Any>("")
        ) {
            is Response.SuccessResponse -> Response.SuccessResponse("adsjaij", LoginData("a@b.c"))
            is Response.ErrorResponse -> throw PenguBankAPIException(response.message)
        }
*/
    }


}