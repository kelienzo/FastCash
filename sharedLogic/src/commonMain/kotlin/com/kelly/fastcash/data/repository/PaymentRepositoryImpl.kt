package com.kelly.fastcash.data.repository

import com.kelly.fastcash.data.remote.PaymentService
import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val api: PaymentService): PaymentRepository {
    override suspend fun processPayment(payment: Payment): Payment {
        return api.processPayment(payment)
    }
}
