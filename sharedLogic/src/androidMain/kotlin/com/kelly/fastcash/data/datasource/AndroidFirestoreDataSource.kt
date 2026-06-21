package com.kelly.fastcash.data.datasource

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.utils.toLocalDateString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AndroidFirestoreDataSource(private val firestore: FirebaseFirestore) : DataSource {

    override suspend fun saveTransaction(transactionsModel: TransactionsModel) {
        val data = mapOf(
            "recipientEmail" to transactionsModel.recipientEmail,
            "amount" to transactionsModel.amount,
            "currency" to transactionsModel.currency,
            "timestamp" to FieldValue.serverTimestamp(),
            "status" to transactionsModel.status
        )
        firestore.collection("payments").add(data).await()
    }

    override fun getTransactions(): Flow<List<TransactionsModel>> {
        return firestore.collection("payments").orderBy("timestamp", Query.Direction.DESCENDING)
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