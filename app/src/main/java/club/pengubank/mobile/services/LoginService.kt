package club.pengubank.mobile.services

import club.pengubank.mobile.api.PenguBankApi
import club.pengubank.mobile.data.requests.LoginRequest
import club.pengubank.mobile.states.StoreState
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*

class LoginService(
    private val store: StoreState,
    private val api: PenguBankApi
) {
    suspend fun login(email: String, password: String): String {
        //val response = api.login(LoginRequest(email, password))
        //store.token = response.token
        return client.get<String>("http://191.168.1.43:8080")

    }

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
        }
    }

}