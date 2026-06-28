package com.kelly.fastcash.data

import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.repository.PaymentRepository

class TestPaymentRepository : PaymentRepository {
    var response: PaymentResponse? = null
    var shouldThrow: Boolean = false

    override suspend fun processPayment(paymentRequest: PaymentRequest): PaymentResponse {
        if (shouldThrow) throw Exception("Network error")
        return response ?: PaymentResponse(
            paymentRequest.amount,
            paymentRequest.currency,
            paymentRequest.recipientEmail,
            true
        )
    }
}