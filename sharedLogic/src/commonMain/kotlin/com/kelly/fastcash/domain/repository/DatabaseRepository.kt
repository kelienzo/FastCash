package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.TransactionsModel
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun saveTransaction(transactionsModel: TransactionsModel)
    fun getTransactions(): Flow<List<TransactionsModel>>
}