package com.kelly.fastcash.domain.usecase

class ValidateEmailUseCase {

    operator fun invoke(email: String): Boolean {
        return email.matches(
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        )
    }
}