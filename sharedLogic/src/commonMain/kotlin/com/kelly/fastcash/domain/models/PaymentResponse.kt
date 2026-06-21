package com.kelly.fastcash.domain.models

data class PaymentResponse(
    val amount: Double?,
    val currency: String?,
    val recipientEmail: String?,
    val status: Boolean
)
