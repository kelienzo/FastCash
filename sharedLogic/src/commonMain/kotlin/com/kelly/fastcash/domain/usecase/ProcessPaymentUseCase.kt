package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.domain.repository.DatabaseRepository
import com.kelly.fastcash.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProcessPaymentUseCase(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateAmountUseCase: ValidateAmountUseCase,
    private val validateCurrencyUseCase: ValidateCurrencyUseCase,
    private val paymentRepository: PaymentRepository,
    private val databaseRepository: DatabaseRepository
) {

    operator fun invoke(paymentRequest: PaymentRequest): Flow<Result<PaymentResponse>> =
        flow<Result<PaymentResponse>> {
            if (!validateEmailUseCase(paymentRequest.recipientEmail)) {
                emit(Result.failure(Throwable("Invalid email address")))
                return@flow
            }
            if (!validateAmountUseCase(paymentRequest.amount.toString())) {
                emit(Result.failure(Throwable("Invalid Amount")))
                return@flow
            }
            if (!validateCurrencyUseCase(paymentRequest.currency)) {
                emit(Result.failure(Throwable("Unsupported currency")))
                return@flow
            }
            val paymentRes = paymentRepository.processPayment(paymentRequest)
            runCatching {
                databaseRepository.saveTransaction(
                    TransactionsModel(
                        recipientEmail = paymentRes.recipientEmail.orEmpty(),
                        amount = paymentRes.amount ?: 0.0,
                        currency = paymentRes.currency.orEmpty(),
                        status = paymentRes.status
                    )
                )
            }.onFailure {
                println("Firestore Save failed $it")
            }
            emit(
                if (paymentRes.status) {
                    Result.success(paymentRes)
                } else {
                    Result.failure(Throwable("Payment failed."))
                }
            )
        }.catch {
            it.printStackTrace()
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
}