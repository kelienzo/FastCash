package com.kelly.fastcash.domain.usecase

class ValidateCurrencyUseCase {

    operator fun invoke(currency: String): Boolean {
        return currency.isNotEmpty()
    }
}