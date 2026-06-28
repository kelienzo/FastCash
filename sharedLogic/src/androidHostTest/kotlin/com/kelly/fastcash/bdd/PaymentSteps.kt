package com.kelly.fastcash.bdd

import com.kelly.fastcash.data.TestDatabaseRepository
import com.kelly.fastcash.data.TestPaymentRepository
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.usecase.GetTransactionsUseCase
import com.kelly.fastcash.domain.usecase.ProcessPaymentUseCase
import com.kelly.fastcash.domain.usecase.ValidateAmountUseCase
import com.kelly.fastcash.domain.usecase.ValidateCurrencyUseCase
import com.kelly.fastcash.domain.usecase.ValidateEmailUseCase
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaymentSteps {

    private val testDatabaseRepository = TestDatabaseRepository()
    private val testPaymentRepository = TestPaymentRepository()
    private val validateEmailUseCase = ValidateEmailUseCase()
    private val validateAmountUseCase = ValidateAmountUseCase()
    private val validateCurrencyUseCase = ValidateCurrencyUseCase()
    private val processPaymentUseCase = ProcessPaymentUseCase(
        validateEmailUseCase,
        validateAmountUseCase,
        validateCurrencyUseCase,
        testPaymentRepository,
        testDatabaseRepository
    )
    private val getTransactionsUseCase = GetTransactionsUseCase(testDatabaseRepository)

    private lateinit var paymentRequest: PaymentRequest
    private var paymentResult: Result<PaymentResponse>? = null
    private var transactionHistory: List<TransactionsModel>? = null

    @Given("a user enters valid payment details {string}, {double}, {string}")
    fun a_user_enters_valid_payment_details(email: String, amount: Double, currency: String) {
        paymentRequest = PaymentRequest(email, amount, currency)
        testPaymentRepository.response = PaymentResponse(amount, currency, email, true)
    }

    @Given("a user enters an invalid amount {string}")
    fun a_user_enters_an_invalid_amount(amount: String) {
        paymentRequest = PaymentRequest("test@test.com", amount.toDouble(), "USD")
    }

    @Given("a user enters an invalid email {string}")
    fun a_user_enters_an_invalid_email(email: String) {
        paymentRequest = PaymentRequest(email, 10.0, "USD")
    }

    @Given("the user has {int} past transactions in the database")
    fun the_user_has_past_transactions_in_the_database(count: Int) = runBlocking {
        repeat(count) {
            testDatabaseRepository.saveTransaction(
                TransactionsModel(
                    recipientEmail = "old$it@test.com",
                    amount = 10.0,
                    currency = "USD",
                    status = true
                )
            )
        }
    }

    @When("they submit the payment")
    fun they_submit_the_payment() = runBlocking {
        paymentResult = processPaymentUseCase(paymentRequest).first()
    }

    @When("they view their transaction history")
    fun they_view_their_transaction_history() = runBlocking {
        transactionHistory = getTransactionsUseCase().first()
    }

    @Then("the payment should be processed successfully")
    fun the_payment_should_be_processed_successfully() {
        assertEquals(paymentResult?.isSuccess, true)
    }

    @Then("the transaction should be saved to the database")
    fun the_transaction_should_be_saved_to_the_database() = runBlocking {
        val transactions = getTransactionsUseCase().first()
        assertTrue(transactions.any { it.recipientEmail == paymentRequest.recipientEmail })
    }

    @Then("the payment should fail")
    fun the_payment_should_fail() {
        assertEquals(paymentResult?.isFailure, true)
    }

    @Then("an error message {string} should be shown")
    fun an_error_message_should_be_shown(expectedMessage: String) {
        assertEquals(expectedMessage, paymentResult?.exceptionOrNull()?.message)
    }

    @Then("they should see {int} transactions in the list")
    fun they_should_see_transactions_in_the_list(expectedCount: Int) {
        assertEquals(expectedCount, transactionHistory?.size)
    }
}
