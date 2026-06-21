package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.Payment
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun saveTransaction(payment: Payment)
    fun getTransactions(): Flow<List<Payment>>
}