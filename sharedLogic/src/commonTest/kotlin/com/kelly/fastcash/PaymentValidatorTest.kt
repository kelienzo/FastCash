package com.kelly.fastcash

import com.kelly.fastcash.domain.validation.PaymentValidator
import com.kelly.fastcash.domain.validation.ValidationResult
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertIs

class PaymentValidatorTest {

    @Test
    fun `Given a valid email, when validated, then return true`() {
        assertTrue(PaymentValidator.validateEmail("test@example.com"))
    }

    @Test
    fun `Given an invalid amount, when validated, then return invalid result`() {
        val result = PaymentValidator.validateAll("test@example.com", -10.0, "USD")
        assertIs<ValidationResult.Invalid>(result)
    }

    @Test
    fun `Given valid details, when validated, then return valid result`() {
        val result = PaymentValidator.validateAll("test@example.com", 100.0, "EUR")
        assertIs<ValidationResult.Valid>(result)
    }
}
