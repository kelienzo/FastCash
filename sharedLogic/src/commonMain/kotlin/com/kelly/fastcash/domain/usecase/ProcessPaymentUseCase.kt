package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.domain.models.Payment
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

    operator fun invoke(payment: Payment): Flow<Result<Payment>> = flow<Result<Payment>> {
        val payment = paymentRepository.processPayment(payment)
        firestoreRepository.saveTransaction(payment)
        emit(Result.success(payment))
    }.catch {
        it.printStackTrace()
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)
}