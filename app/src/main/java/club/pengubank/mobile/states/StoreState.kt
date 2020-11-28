package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.data.Key
import club.pengubank.mobile.data.User

class StoreState {
    var token by mutableStateOf("")
    //var user by mutableStateOf<User?>(null)
    var secretKey by mutableStateOf<Key?>(null)
    var user by mutableStateOf<User?>(User(1, "a@b.c", "Today", true, 1))

    fun logout() {
        token = ""
        user = null
        secretKey = null
    }

    fun isLoggedIn(): Boolean = token.isNotBlank() && user != null
}