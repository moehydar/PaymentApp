package com.cashi.payment.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashi.payment.android.data.PaymentUiState
import com.cashi.payment.model.Currency
import com.cashi.payment.repository.PaymentRepository
import com.cashi.payment.validation.PaymentValidator
import com.cashi.payment.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class PaymentViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            recipientEmail = email,
            emailError = null,
            errorMessage = null,
            successMessage = null
        )
    }

    fun updateAmount(amount: String) {
        // only allow valid number input
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _uiState.value = _uiState.value.copy(
                amount = amount,
                amountError = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun updateCurrency(currency: Currency) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency = currency,
            errorMessage = null,
            successMessage = null
        )
    }

    fun sendPayment() {
        // clear previous messages
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null,
            emailError = null,
            amountError = null
        )

        // validate inputs
        val emailValidation = PaymentValidator.validateEmail(_uiState.value.recipientEmail)
        if (emailValidation is ValidationResult.Invalid) {
            _uiState.value = _uiState.value.copy(emailError = emailValidation.message)
            return
        }

        val amountValue = _uiState.value.amount.toDoubleOrNull() ?: 0.0
        val amountValidation = PaymentValidator.validateAmount(amountValue)
        if (amountValidation is ValidationResult.Invalid) {
            _uiState.value = _uiState.value.copy(amountError = amountValidation.message)
            return
        }

        //send payment
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            paymentRepository.sendPayment(
                recipientEmail = _uiState.value.recipientEmail,
                amount = amountValue,
                currency = _uiState.value.selectedCurrency
            ).fold(
                onSuccess = { payment ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Payment sent successfully to ${payment.recipientEmail}",
                        recipientEmail = "",
                        amount = ""
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to send payment"
                    )
                }
            )
        }
    }
}