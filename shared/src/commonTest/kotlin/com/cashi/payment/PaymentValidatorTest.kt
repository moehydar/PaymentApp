package com.cashi.payment

import com.cashi.payment.validation.PaymentValidator
import com.cashi.payment.validation.ValidationResult
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class PaymentValidatorTest {

    @Test
    fun testValidEmail() {
        val result = PaymentValidator.validateEmail("test@example.com")
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testInvalidEmail() {
        val result = PaymentValidator.validateEmail("test@")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Invalid email format", result.message)
    }

    @Test
    fun testEmailWithSpecialCharacters() {
        val result = PaymentValidator.validateEmail("test.user+tag@example.com")
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testValidAmount() {
        val result = PaymentValidator.validateAmount(100.50)
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testZeroAmount() {
        val result = PaymentValidator.validateAmount(0.0)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Amount must be greater than 0", result.message)
    }

    @Test
    fun testNegativeAmount() {
        val result = PaymentValidator.validateAmount(-50.0)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Amount must be greater than 0", result.message)
    }

    @Test
    fun testAmountExceedsLimit() {
        val result = PaymentValidator.validateAmount(10001.0)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Amount cannot exceed 10,000", result.message)
    }

    @Test
    fun testValidCurrency() {
        val result = PaymentValidator.validateCurrency("USD")
        assertTrue(result is ValidationResult.Valid)

        val result2 = PaymentValidator.validateCurrency("EUR")
        assertTrue(result2 is ValidationResult.Valid)
    }

    @Test
    fun testInvalidCurrency() {
        val result = PaymentValidator.validateCurrency("GBP")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Invalid currency. Use USD or EUR", result.message)
    }

    @Test
    fun testValidatePaymentSuccess() {
        val result = PaymentValidator.validatePayment(
            email = "test@example.com",
            amount = 50.0,
            currency = "USD"
        )
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun testValidatePaymentInvalidEmail() {
        val result = PaymentValidator.validatePayment(
            email = "invalid",
            amount = 50.0,
            currency = "USD"
        )
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Invalid email format", result.message)
    }
}