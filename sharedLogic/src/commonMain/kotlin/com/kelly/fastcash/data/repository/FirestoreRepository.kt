package com.kelly.fastcash.data.repository

import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow

expect class FirestoreRepositoryImpl: FirestoreRepository {
    override suspend fun saveTransaction(payment: Payment)
    override fun getTransactions(): Flow<List<Payment>>
}