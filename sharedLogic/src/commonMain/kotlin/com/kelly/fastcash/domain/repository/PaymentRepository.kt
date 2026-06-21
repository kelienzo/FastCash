package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.models.PaymentResponse

interface PaymentRepository {

    suspend fun processPayment(payment: Payment): PaymentResponse
}