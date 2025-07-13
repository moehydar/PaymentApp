package com.cashi.payment.validation

import com.cashi.payment.model.Currency

object PaymentValidator {

    fun validateEmail(email: String): ValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return if (email.matches(emailRegex)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid("Invalid email format")
        }
    }

    fun validateAmount(amount: Double): ValidationResult {
        return when {
            amount <= 0 -> ValidationResult.Invalid("Amount must be greater than 0")
            amount > 10000 -> ValidationResult.Invalid("Amount cannot exceed 10,000")
            else -> ValidationResult.Valid
        }
    }

    fun validateCurrency(currencyString: String): ValidationResult {
        return try {
            Currency.valueOf(currencyString)
            ValidationResult.Valid
        } catch (e: IllegalArgumentException) {
            ValidationResult.Invalid("Invalid currency. Use USD or EUR")
        }
    }

    fun validatePayment(
        email: String,
        amount: Double,
        currency: String
    ): ValidationResult {
        val emailValidation = validateEmail(email)
        if (emailValidation is ValidationResult.Invalid) return emailValidation

        val amountValidation = validateAmount(amount)
        if (amountValidation is ValidationResult.Invalid) return amountValidation

        val currencyValidation = validateCurrency(currency)
        if (currencyValidation is ValidationResult.Invalid) return currencyValidation

        return ValidationResult.Valid
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}