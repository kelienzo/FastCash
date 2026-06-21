package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.data.TestFirestoreRepository
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProcessPaymentUseCaseTest {

    private lateinit var fakePaymentRepository: FakePaymentRepository
    private lateinit var testFirestoreRepository: TestFirestoreRepository
    private lateinit var processPaymentUseCase: ProcessPaymentUseCase

    @BeforeTest
    fun setup() {
        fakePaymentRepository = FakePaymentRepository()
        testFirestoreRepository = TestFirestoreRepository()
        processPaymentUseCase = ProcessPaymentUseCase(
            fakePaymentRepository,
            testFirestoreRepository
        )
    }

    @Test
    fun `when payment is successful, then it saves transaction and returns success`() = runTest {
        // Given
        val request = PaymentRequest("user@example.com", 100.0, "USD")
        val expectedResponse = PaymentResponse(100.0, "USD", "user@example.com", true)
        fakePaymentRepository.response = expectedResponse

        // When
        val result = processPaymentUseCase(request).first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedResponse, result.getOrNull())

        // Verify transaction saved
        val transactionSuccessful = testFirestoreRepository.getTransactions().first().firstOrNull()
        assertEquals(request.recipientEmail, transactionSuccessful?.recipientEmail)
        assertEquals(request.amount, transactionSuccessful?.amount)
        assertEquals(request.currency, transactionSuccessful?.currency)
        assertEquals(true, transactionSuccessful?.status)
    }

    @Test
    fun `when payment fails, then it saves transaction and returns failure`() =
        runTest {
            // Given
            val request = PaymentRequest("user@example.com", 100.0, "USD")
            val expectedResponse = PaymentResponse(100.0, "USD", "user@example.com", false)
            fakePaymentRepository.response = expectedResponse

            // When
            val result = processPaymentUseCase(request).first()

            // Then
            assertTrue(result.isFailure)
            assertEquals("Payment failed.", result.exceptionOrNull()?.message)

            // Verify transaction saved
            val failedTransaction = testFirestoreRepository.getTransactions().first().firstOrNull()
            assertEquals(request.recipientEmail, failedTransaction?.recipientEmail)
            assertEquals(request.amount, failedTransaction?.amount)
            assertEquals(request.currency, failedTransaction?.currency)
            assertEquals(false, failedTransaction?.status)
        }

    @Test
    fun `when repository throws exception, and transaction is not saved then it returns failure result`() =
        runTest {
            // Given
            val request = PaymentRequest("user@example.com", 100.0, "USD")
            fakePaymentRepository.shouldThrow = true

            // When
            val result = processPaymentUseCase(request).first()

            // Then
            assertTrue(result.isFailure)
            assertEquals("Network error", result.exceptionOrNull()?.message)
            assertEquals(true, testFirestoreRepository.getTransactions().first().isEmpty())
        }

    private class FakePaymentRepository : PaymentRepository {
        var response: PaymentResponse? = null
        var shouldThrow: Boolean = false

        override suspend fun processPayment(paymentRequest: PaymentRequest): PaymentResponse {
            if (shouldThrow) throw Exception("Network error")
            return response ?: PaymentResponse(
                paymentRequest.amount,
                paymentRequest.currency,
                paymentRequest.recipientEmail,
                true
            )
        }
    }
}
