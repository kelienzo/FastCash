package com.kelly.fastcash.data.remote

import com.kelly.fastcash.data.model.PaymentRequestDTO
import com.kelly.fastcash.data.model.PaymentResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PaymentService(private val client: HttpClient) {
    private val baseUrl = "https://6a36bb11766b831960f9816a.mockapi.io"

    suspend fun processPayment(paymentRequestDTO: PaymentRequestDTO): PaymentResponseDTO {
        return client.post("$baseUrl/payments") {
            setBody(paymentRequestDTO)
        }.body()
    }
}
