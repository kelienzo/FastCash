package com.kelly.fastcash.domain.usecase

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateAmountUseCaseTest {

    private lateinit var validateAmountUseCase: ValidateAmountUseCase

    @BeforeTest
    fun setup() {
        validateAmountUseCase = ValidateAmountUseCase()
    }

    @Test
    fun `Given a valid integer amount, when validated, then return true`() {
        val amount = "100"
        assertTrue(validateAmountUseCase(amount), "Amount $amount should be valid")
    }

    @Test
    fun `Given a valid decimal amount with one digit, when validated, then return true`() {
        val amount = "10.5"
        assertTrue(validateAmountUseCase(amount), "Amount $amount should be valid")
    }

    @Test
    fun `Given a valid decimal amount with two digits, when validated, then return true`() {
        val amount = "10.99"
        assertTrue(validateAmountUseCase(amount), "Amount $amount should be valid")
    }

    @Test
    fun `Given a decimal amount with three digits, when validated, then return false`() {
        val amount = "10.999"
        assertFalse(validateAmountUseCase(amount), "Amount $amount should be invalid (too many decimal places)")
    }

    @Test
    fun `Given a negative amount, when validated, then return false`() {
        val amount = "-10"
        assertFalse(validateAmountUseCase(amount), "Amount $amount should be invalid (negative)")
    }

    @Test
    fun `Given an empty string, when validated, then return false`() {
        val amount = ""
        assertFalse(validateAmountUseCase(amount), "Empty amount should be invalid")
    }

    @Test
    fun `Given non-numeric characters, when validated, then return false`() {
        val amount = "10abc"
        assertFalse(validateAmountUseCase(amount), "Amount $amount should be invalid (contains letters)")
    }

    @Test
    fun `Given just a decimal point, when validated, then return false`() {
        val amount = "."
        assertFalse(validateAmountUseCase(amount), "Just a decimal point should be invalid")
    }

    @Test
    fun `Given a decimal point without leading digits, when validated, then return false`() {
        val amount = ".50"
        assertFalse(validateAmountUseCase(amount), "Amount $amount should be invalid (missing leading digits)")
    }
}
