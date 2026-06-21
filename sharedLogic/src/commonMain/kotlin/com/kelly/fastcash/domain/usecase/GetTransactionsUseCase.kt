package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.domain.repository.FirestoreRepository

class GetTransactionsUseCase(private val firestoreRepository: FirestoreRepository) {

    operator fun invoke() = firestoreRepository.getTransactions()
}