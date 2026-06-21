package com.kelly.fastcash.domain.usecase

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateCurrencyUseCaseTest {

    private lateinit var validateCurrencyUseCase: ValidateCurrencyUseCase

    @BeforeTest
    fun setup() {
        validateCurrencyUseCase = ValidateCurrencyUseCase()
    }

    @Test
    fun `Given a non-empty currency string, when validated, then return true`() {
        val currency = "USD"
        assertTrue(validateCurrencyUseCase(currency), "Currency $currency should be valid")
    }

    @Test
    fun `Given another non-empty currency string, when validated, then return true`() {
        val currency = "EUR"
        assertTrue(validateCurrencyUseCase(currency), "Currency $currency should be valid")
    }

    @Test
    fun `Given an empty string, when validated, then return false`() {
        val currency = ""
        assertFalse(validateCurrencyUseCase(currency), "Empty currency should be invalid")
    }
}
