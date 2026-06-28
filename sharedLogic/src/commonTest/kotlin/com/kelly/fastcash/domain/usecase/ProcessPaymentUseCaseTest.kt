package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.data.TestDatabaseRepository
import com.kelly.fastcash.data.TestPaymentRepository
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProcessPaymentUseCaseTest {

    private lateinit var testPaymentRepository: TestPaymentRepository
    private lateinit var testDatabaseRepository: TestDatabaseRepository
    private lateinit var processPaymentUseCase: ProcessPaymentUseCase
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validateAmountUseCase: ValidateAmountUseCase
    private lateinit var validateCurrencyUseCase: ValidateCurrencyUseCase

    @BeforeTest
    fun setup() {
        validateEmailUseCase = ValidateEmailUseCase()
        validateAmountUseCase = ValidateAmountUseCase()
        validateCurrencyUseCase = ValidateCurrencyUseCase()
        testPaymentRepository = TestPaymentRepository()
        testDatabaseRepository = TestDatabaseRepository()
        processPaymentUseCase = ProcessPaymentUseCase(
            validateEmailUseCase,
            validateAmountUseCase,
            validateCurrencyUseCase,
            testPaymentRepository,
            testDatabaseRepository,

            )
    }

    @Test
    fun `when payment is successful, then it saves transaction and returns success`() = runTest {
        // Given
        val request = PaymentRequest("user@example.com", 100.0, "USD")
        val expectedResponse = PaymentResponse(100.0, "USD", "user@example.com", true)
        testPaymentRepository.response = expectedResponse

        // When
        val result = processPaymentUseCase(request).first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedResponse, result.getOrNull())

        // Verify transaction saved
        val transactionSuccessful = testDatabaseRepository.getTransactions().first().firstOrNull()
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
            testPaymentRepository.response = expectedResponse

            // When
            val result = processPaymentUseCase(request).first()

            // Then
            assertTrue(result.isFailure)
            assertEquals("Payment failed.", result.exceptionOrNull()?.message)

            // Verify transaction saved
            val failedTransaction = testDatabaseRepository.getTransactions().first().firstOrNull()
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
            testPaymentRepository.shouldThrow = true

            // When
            val result = processPaymentUseCase(request).first()

            // Then
            assertTrue(result.isFailure)
            assertEquals("Network error", result.exceptionOrNull()?.message)
            assertEquals(true, testDatabaseRepository.getTransactions().first().isEmpty())
        }
}
