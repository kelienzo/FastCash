package com.kelly.fastcash.domain.usecase

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateEmailUseCaseTest {

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @BeforeTest
    fun setup() {
        validateEmailUseCase = ValidateEmailUseCase()
    }

    @Test
    fun `Given a valid simple email, when validated, then return true`() {
        val email = "user@example.com"
        assertTrue(validateEmailUseCase(email), "Email $email should be valid")
    }

    @Test
    fun `Given a valid email with special characters, when validated, then return true`() {
        val email = "user.name+tag@sub.example.co"
        assertTrue(validateEmailUseCase(email), "Email $email should be valid")
    }

    @Test
    fun `Given an email without @ symbol, when validated, then return false`() {
        val email = "userexample.com"
        assertFalse(validateEmailUseCase(email), "Email $email should be invalid (missing @)")
    }

    @Test
    fun `Given an email without domain, when validated, then return false`() {
        val email = "user@"
        assertFalse(validateEmailUseCase(email), "Email $email should be invalid (missing domain)")
    }

    @Test
    fun `Given an email with missing TLD, when validated, then return false`() {
        val email = "user@example"
        assertFalse(validateEmailUseCase(email), "Email $email should be invalid (missing TLD)")
    }

    @Test
    fun `Given an empty string, when validated, then return false`() {
        val email = ""
        assertFalse(validateEmailUseCase(email), "Empty email should be invalid")
    }

    @Test
    fun `Given an email with invalid characters, when validated, then return false`() {
        val email = "user space@example.com"
        assertFalse(validateEmailUseCase(email), "Email with space should be invalid")
    }
}
