package com.cashi.payment.android.data

import com.cashi.payment.model.Currency

data class PaymentUiState(
    val recipientEmail: String = "",
    val amount: String = "",
    val selectedCurrency: Currency = Currency.USD,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val emailError: String? = null,
    val amountError: String? = null
)