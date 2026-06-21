package com.kelly.fastcash.data.repository

import com.kelly.fastcash.data.datasource.DataSource
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(private val dataSource: DataSource) : DatabaseRepository {

    override suspend fun saveTransaction(transactionsModel: TransactionsModel) {
        dataSource.saveTransaction(transactionsModel)
    }

    override fun getTransactions(): Flow<List<TransactionsModel>> {
        return dataSource.getTransactions()
    }
}