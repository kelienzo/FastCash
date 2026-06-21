package com.kelly.fastcash.domain.usecase

class ValidateAmountUseCase {

    operator fun invoke(amount: String): Boolean {
        return amount.matches(Regex("^[0-9]+(\\.[0-9]{1,2})?$"))
    }
}