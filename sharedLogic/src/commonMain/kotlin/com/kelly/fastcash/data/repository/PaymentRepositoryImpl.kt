package com.kelly.fastcash.data.repository

import com.kelly.fastcash.data.mapper.toPaymentRequestDTO
import com.kelly.fastcash.data.mapper.toPaymentResponse
import com.kelly.fastcash.data.remote.PaymentService
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val api: PaymentService) : PaymentRepository {
    override suspend fun processPayment(paymentRequest: PaymentRequest): PaymentResponse {
        return api.processPayment(paymentRequest.toPaymentRequestDTO()).toPaymentResponse()
    }
}
