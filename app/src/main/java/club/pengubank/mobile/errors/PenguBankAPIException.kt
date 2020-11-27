package club.pengubank.mobile.errors

import java.lang.RuntimeException

class PenguBankAPIException(override val message: String) : RuntimeException()
