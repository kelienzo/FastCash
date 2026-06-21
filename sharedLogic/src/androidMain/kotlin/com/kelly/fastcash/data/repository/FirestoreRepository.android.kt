package com.kelly.fastcash.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

actual class FirestoreRepositoryImpl : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("transactions")

    actual override suspend fun saveTransaction(payment: Payment) {
        val data = mapOf(
            "recipientEmail" to payment.recipientEmail,
            "amount" to payment.amount,
            "currency" to payment.currency,
            "timestamp" to System.currentTimeMillis()
        )
        collection.add(data).await()
    }

    actual override fun getTransactions(): Flow<List<Payment>> {
        return collection.orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    Payment(
                        id = doc.id,
                        recipientEmail = doc.getString("recipientEmail") ?: "",
                        amount = doc.getDouble("amount") ?: 0.0,
                        currency = doc.getString("currency") ?: "USD",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
            }
    }
}