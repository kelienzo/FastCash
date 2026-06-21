package com.kelly.fastcash.data.repository

import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class FirestoreRepositoryImpl : FirestoreRepository {
    actual override suspend fun saveTransaction(transactionsModel: TransactionsModel) {
    }

    actual override fun getTransactions(): Flow<List<TransactionsModel>> {
        return emptyFlow()
    }
}