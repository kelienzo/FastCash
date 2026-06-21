package com.kelly.fastcash.data.datasource

import com.kelly.fastcash.domain.models.TransactionsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class IosFirestoreDataSource : DataSource {

    override suspend fun saveTransaction(transactionsModel: TransactionsModel) {

    }

    override fun getTransactions(): Flow<List<TransactionsModel>> {
        return emptyFlow()
    }
}