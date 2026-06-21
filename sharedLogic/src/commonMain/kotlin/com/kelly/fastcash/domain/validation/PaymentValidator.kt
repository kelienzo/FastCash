package com.kelly.fastcash.domain.validation

object PaymentValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun validateEmail(email: String): Boolean {
        return email.isNotBlank() && emailRegex.matches(email)
    }

    fun validateAmount(amount: Double): Boolean {
        return amount > 0
    }

    fun validateCurrency(currency: String): Boolean {
        return currency == "USD" || currency == "EUR"
    }

    fun validateAll(email: String, amount: Double, currency: String): ValidationResult {
        if (!validateEmail(email)) return ValidationResult.Invalid("Invalid recipient email")
        if (!validateAmount(amount)) return ValidationResult.Invalid("Amount must be greater than zero")
        if (!validateCurrency(currency)) return ValidationResult.Invalid("Unsupported currency")
        return ValidationResult.Valid
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}
