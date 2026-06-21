package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.data.TestFirestoreRepository
import com.kelly.fastcash.domain.models.TransactionsModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTransactionsUseCaseTest {

    private lateinit var testFirestoreRepository: TestFirestoreRepository

    @BeforeTest
    fun setUp() {
        testFirestoreRepository = TestFirestoreRepository()
    }

    @Test
    fun `when invoke is called, then it returns transactions from repository`() = runTest {
        // Given
        val expectedTransactions = listOf(
            TransactionsModel(
                id = "1",
                recipientEmail = "test@example.com",
                amount = 100.0,
                currency = "USD",
                timestamp = "2023-10-27T10:00:00Z",
                status = true
            ),
            TransactionsModel(
                id = "2",
                recipientEmail = "another@example.com",
                amount = 50.0,
                currency = "EUR",
                timestamp = "2023-10-27T11:00:00Z",
                status = false
            )
        )
        expectedTransactions.forEach {
            testFirestoreRepository.saveTransaction(it)
        }
        val useCase = GetTransactionsUseCase(testFirestoreRepository)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedTransactions, result)
        assertEquals(2, result.size)
        assertEquals("test@example.com", result[0].recipientEmail)
    }

    @Test
    fun `when repository is empty, then invoke returns an empty list`() = runTest {
        val useCase = GetTransactionsUseCase(testFirestoreRepository)

        val result = useCase().first()

        assertEquals(true, result.isEmpty())
    }
}
