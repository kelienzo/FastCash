package com.kelly.fastcash.data.repository

import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow

actual class FirestoreRepositoryImpl : FirestoreRepository {
    actual override suspend fun saveTransaction(payment: Payment) {
    }

    actual override fun getTransactions(): Flow<List<Payment>> {
        TODO("Not yet implemented")
    }
}