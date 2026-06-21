package com.kelly.fastcash.data.mapper

import com.kelly.fastcash.data.model.PaymentRequestDTO
import com.kelly.fastcash.data.model.PaymentResponseDTO
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import kotlin.random.Random

fun PaymentResponseDTO.toPaymentResponse() = PaymentResponse(
    amount = amount,
    currency = currency,
    recipientEmail = recipientEmail,
    status = transactionStatus
)

fun PaymentRequest.toPaymentRequestDTO() = PaymentRequestDTO(
    recipientEmail = recipientEmail,
    amount = amount,
    currency = currency,
    transactionStatus = Random.nextBoolean()
)