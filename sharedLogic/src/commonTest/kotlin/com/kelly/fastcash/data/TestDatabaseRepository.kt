package com.kelly.fastcash.data

import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestDatabaseRepository : DatabaseRepository {
    var transactions = mutableListOf<TransactionsModel>()

    override suspend fun saveTransaction(transactionsModel: TransactionsModel) {
        transactions.add(transactionsModel)
    }

    override fun getTransactions(): Flow<List<TransactionsModel>> {
        return flowOf(transactions)
    }
}