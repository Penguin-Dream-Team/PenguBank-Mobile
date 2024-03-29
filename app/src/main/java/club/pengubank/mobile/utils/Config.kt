package club.pengubank.mobile.utils

import com.sksamuel.hoplite.ConfigLoader

data class Config(
    val protocol: String,
    val host: String,
    val port: Int = 80,
    val timeout: Long
) {
    companion object {
        fun load() = ConfigLoader().loadConfigOrThrow<Config>("/api_config.json")
    }
}
