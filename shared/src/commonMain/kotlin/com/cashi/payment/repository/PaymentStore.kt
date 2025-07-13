package com.cashi.payment.repository

import com.cashi.payment.model.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentStore {
    suspend fun savePayment(payment: Payment)
    suspend fun getAllPayments(): List<Payment>
    fun observePayments(): Flow<List<Payment>>
}