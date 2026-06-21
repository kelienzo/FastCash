package com.kelly.fastcash.data.mapper

import com.kelly.fastcash.data.model.PaymentResponseDTO
import com.kelly.fastcash.domain.models.PaymentResponse

fun PaymentResponseDTO.toPaymentResponse() = PaymentResponse(
    id = id,
    createdAt = createdAt,
    status = status
)