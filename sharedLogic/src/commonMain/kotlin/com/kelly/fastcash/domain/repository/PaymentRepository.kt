package com.kelly.fastcash.domain.repository

import com.kelly.fastcash.domain.models.Payment

interface PaymentRepository {

    suspend fun processPayment(payment: Payment): Payment
}