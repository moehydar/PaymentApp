package com.cashi.payment.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashi.payment.model.Payment
import com.cashi.payment.repository.PaymentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    val payments: StateFlow<List<Payment>> = paymentRepository.payments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        paymentRepository.initializePaymentObservation(viewModelScope)

        //load payments from Firebase on initialization
        viewModelScope.launch {
            paymentRepository.loadPayments()
        }
    }
}