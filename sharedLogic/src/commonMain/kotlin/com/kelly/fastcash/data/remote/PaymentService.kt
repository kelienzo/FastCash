package com.kelly.fastcash.data.remote

import com.kelly.fastcash.data.model.PaymentResponseDTO
import com.kelly.fastcash.domain.models.Payment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PaymentService(private val client: HttpClient) {
    private val baseUrl = "https://6a36bb11766b831960f9816a.mockapi.io"

    suspend fun processPayment(payment: Payment): List<PaymentResponseDTO> {
        return client.post("$baseUrl/payments") {
            setBody(payment)
        }.body()
    }
}
