package com.kelly.fastcash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequestDTO(
    val recipientEmail: String,
    val amount: Double,
    val currency: String,
    val transactionStatus: Boolean
)
