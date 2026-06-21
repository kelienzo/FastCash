package com.kelly.fastcash.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.FirestoreRepository
import com.kelly.fastcash.utils.toLocalDateString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

actual class FirestoreRepositoryImpl : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("payments")

    actual override suspend fun saveTransaction(transactionsModel: TransactionsModel) {
        Log.d("Save transactions", "I got here")
        val data = mapOf(
            "recipientEmail" to transactionsModel.recipientEmail,
            "amount" to transactionsModel.amount,
            "currency" to transactionsModel.currency,
            "timestamp" to FieldValue.serverTimestamp(),
            "status" to transactionsModel.status
        )
        collection.add(data).await()
    }

    actual override fun getTransactions(): Flow<List<TransactionsModel>> {
        Log.d("Get transactions", "I got here")
        return collection.orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    TransactionsModel(
                        id = doc.id,
                        recipientEmail = doc.getString("recipientEmail") ?: "",
                        amount = doc.getDouble("amount") ?: 0.0,
                        currency = doc.getString("currency") ?: "USD",
                        timestamp = doc.getTimestamp("timestamp")
                            ?.toDate()?.time?.toLocalDateString() ?: "",
                        status = doc.getBoolean("status") ?: false
                    )
                }
            }
    }
}