package com.kelly.fastcash.domain.models

data class PaymentRequest(
    val recipientEmail: String,
    val amount: Double,
    val currency: String
)
