package com.kelly.fastcash.domain.usecase

import com.kelly.fastcash.domain.repository.DatabaseRepository

class GetTransactionsUseCase(private val databaseRepository: DatabaseRepository) {

    operator fun invoke() = databaseRepository.getTransactions()
}