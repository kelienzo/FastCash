package com.kelly.fastcash.data.repository

import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow

expect class FirestoreRepositoryImpl: FirestoreRepository {
    override suspend fun saveTransaction(transactionsModel: TransactionsModel)
    override fun getTransactions(): Flow<List<TransactionsModel>>
}