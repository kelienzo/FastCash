package com.kelly.fastcash.data.datasource

import com.kelly.fastcash.domain.models.TransactionsModel
import kotlinx.coroutines.flow.Flow

interface DataSource {

    suspend fun saveTransaction(transactionsModel: TransactionsModel)

    fun getTransactions(): Flow<List<TransactionsModel>>
}