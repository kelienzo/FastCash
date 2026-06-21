package com.kelly.fastcash.data.remote

import com.kelly.fastcash.domain.models.Payment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PaymentService(private val client: HttpClient) {
    private val baseUrl = "https://your-mock-api.com" 

    suspend fun processPayment(payment: Payment): Payment {
        return client.post("$baseUrl/payments") {
            setBody(payment)
        }.body()
    }
}
