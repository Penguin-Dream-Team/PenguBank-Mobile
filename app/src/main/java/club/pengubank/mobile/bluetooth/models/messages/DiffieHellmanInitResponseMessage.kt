package club.pengubank.mobile.bluetooth.models.messages

import club.pengubank.mobile.bluetooth.models.JSONObject
import java.math.BigInteger

data class DiffieHellmanInitResponseMessage(val publicY: BigInteger) : JSONObject