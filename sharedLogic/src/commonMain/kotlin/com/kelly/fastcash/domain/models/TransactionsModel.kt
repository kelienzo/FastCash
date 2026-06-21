package com.kelly.fastcash.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsModel(
    val id: String? = null,
    val recipientEmail: String,
    val amount: Double,
    val currency: String,
    val timestamp: String = "",
    val status: Boolean
)
