package com.kelly.fastcash.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val id: String? = null,
    val recipientEmail: String,
    val amount: Double,
    val currency: String,
    val timestamp: Long = 0L
)
