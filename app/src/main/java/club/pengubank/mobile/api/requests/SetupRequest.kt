package club.pengubank.mobile.api.requests

data class SetupRequest(val phonePublicKey: String, var phoneMACAddress: String = "")
