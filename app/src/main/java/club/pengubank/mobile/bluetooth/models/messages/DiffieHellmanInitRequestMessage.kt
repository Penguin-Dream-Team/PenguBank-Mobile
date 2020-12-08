package club.pengubank.mobile.bluetooth.models.messages

import club.pengubank.mobile.bluetooth.models.JSONObject
import java.math.BigInteger

data class DiffieHellmanInitRequestMessage(val publicY: BigInteger) : JSONObject