package com.cashi.payment.model

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val id: String,
    val recipientEmail: String,
    val amount: Double,
    val currency: Currency,
    val status: PaymentStatus,
    val timestamp: Long
)

@Serializable
enum class Currency {
    USD, EUR
}

@Serializable
enum class PaymentStatus {
    SUCCESS, FAILED
}

@Serializable
data class PaymentRequest(
    val recipientEmail: String,
    val amount: Double,
    val currency: String
)

@Serializable
data class PaymentResponse(
    val id: String,
    val recipientEmail: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val timestamp: Long
)

@Serializable
data class ErrorResponse(
    val error: String
)

class PaymentException(message: String) : Exception(message)