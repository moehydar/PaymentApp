package com.cashi.payment.repository

import com.cashi.payment.model.Currency
import com.cashi.payment.model.Payment
import com.cashi.payment.model.PaymentException
import com.cashi.payment.model.PaymentStatus
import com.cashi.payment.network.PaymentApiClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentRepository(
    private val apiClient: PaymentApiClient = PaymentApiClient(),
    private val paymentStore: PaymentStore? = null
) {
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // use view-model scope so that we don't leak the repository
    fun initializePaymentObservation(scope: CoroutineScope) {
        paymentStore?.let { store ->
            scope.launch {
                store.observePayments().collect { payments ->
                    _payments.value = payments
                }
            }
        }
    }

    suspend fun sendPayment(
        recipientEmail: String,
        amount: Double,
        currency: Currency
    ): Result<Payment> {
        _isLoading.value = true

        return try {
            val result = apiClient.processPayment(
                recipientEmail = recipientEmail,
                amount = amount,
                currency = currency.name
            )

            result.fold(
                onSuccess = { response ->
                    val payment = Payment(
                        id = response.id,
                        recipientEmail = response.recipientEmail,
                        amount = response.amount,
                        currency = Currency.valueOf(response.currency),
                        status = when (response.status) {
                            "success" -> PaymentStatus.SUCCESS
                            "failed" -> PaymentStatus.FAILED
                            else -> throw PaymentException("Unknown payment status: ${response.status}")
                        },
                        timestamp = response.timestamp
                    )

                    paymentStore?.savePayment(payment)

                    Result.success(payment)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(PaymentException("Failed to process payment: ${e.message}"))
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadPayments() {
        paymentStore?.let { store ->
            try {
                val payments = store.getAllPayments()
                _payments.value = payments
            } catch (e: Exception) {
            }
        }
    }


}