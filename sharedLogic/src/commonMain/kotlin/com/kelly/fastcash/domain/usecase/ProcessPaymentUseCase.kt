package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.domain.models.Payment
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.repository.FirestoreRepository
import com.kelly.fastcash.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProcessPaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val firestoreRepository: FirestoreRepository
) {

    operator fun invoke(payment: Payment): Flow<Result<PaymentResponse>> =
        flow<Result<PaymentResponse>> {
            val paymentRes = paymentRepository.processPayment(payment)
            if (paymentRes.status) {
                firestoreRepository.saveTransaction(payment)
                emit(Result.success(paymentRes))
            } else {
                emit(Result.failure(Throwable("Payment failed.")))
            }
        }.catch {
            it.printStackTrace()
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
}