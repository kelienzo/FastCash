package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.TransactionsModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun saveTransaction(transactionsModel: TransactionsModel)
    fun getTransactions(): Flow<List<TransactionsModel>>
}