package com.kelly.fastcash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponseDTO(
    val amount: Double,
    val createdAt: String,
    val currency: String,
    val id: String,
    val recipientEmail: String,
    val transactionStatus: Boolean
)