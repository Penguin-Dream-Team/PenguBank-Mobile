package club.pengubank.mobile.services

import club.pengubank.mobile.data.QueuedTransaction
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.storage.UserDataService

class TransactionService(
    private val userDataService: UserDataService,
    private val store: StoreState
) {

    fun updateTransaction(action: String, transaction: QueuedTransaction) {
        // call Desktop UpdateTransaction
    }
}