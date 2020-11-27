package club.pengubank.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import club.pengubank.mobile.data.User

class StoreState {
    var token by mutableStateOf("")
    var user by mutableStateOf<User?>(null)
}