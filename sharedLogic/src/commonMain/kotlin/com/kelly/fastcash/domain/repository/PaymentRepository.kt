package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.models.PaymentResponse

interface PaymentRepository {

    suspend fun processPayment(paymentRequest: PaymentRequest): PaymentResponse
}