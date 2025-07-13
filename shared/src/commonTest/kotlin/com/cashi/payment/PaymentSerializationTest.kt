package com.cashi.payment

import com.cashi.payment.model.ErrorResponse
import com.cashi.payment.model.PaymentRequest
import com.cashi.payment.model.PaymentResponse
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun testPaymentRequestSerialization() {
        val request = PaymentRequest(
            recipientEmail = "test@example.com",
            amount = 100.50,
            currency = "USD"
        )

        val jsonString = json.encodeToString(PaymentRequest.serializer(), request)
        val decoded = json.decodeFromString(PaymentRequest.serializer(), jsonString)

        assertEquals(request.recipientEmail, decoded.recipientEmail)
        assertEquals(request.amount, decoded.amount)
        assertEquals(request.currency, decoded.currency)
    }

    @Test
    fun testPaymentResponseDeserialization() {
        val jsonString = """
            {
                "id": "payment_123",
                "recipientEmail": "test@example.com",
                "amount": 100.5,
                "currency": "USD",
                "status": "success",
                "timestamp": 1234567890
            }
        """.trimIndent()

        val response = json.decodeFromString(PaymentResponse.serializer(), jsonString)

        assertEquals("payment_123", response.id)
        assertEquals("test@example.com", response.recipientEmail)
        assertEquals(100.5, response.amount)
        assertEquals("USD", response.currency)
        assertEquals("success", response.status)
        assertEquals(1234567890L, response.timestamp)
    }

    @Test
    fun testErroResponseDeserialization() {
        val jsonString = """
            {
                "error": "Invalid email format"
            }
        """.trimIndent()

        val response = json.decodeFromString(ErrorResponse.serializer(), jsonString)

        assertEquals("Invalid email format", response.error)
    }
}