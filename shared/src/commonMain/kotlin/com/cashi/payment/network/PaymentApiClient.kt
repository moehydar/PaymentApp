package com.cashi.payment.network

import com.cashi.payment.model.PaymentRequest
import com.cashi.payment.model.PaymentResponse
import com.cashi.payment.model.ErrorResponse
import com.cashi.payment.model.PaymentException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class PaymentApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun processPayment(
        recipientEmail: String,
        amount: Double,
        currency: String
    ): Result<PaymentResponse> {
        return try {
            val response = client.post("http://10.0.2.2:8080/payments") {
                contentType(ContentType.Application.Json)
                setBody(PaymentRequest(recipientEmail, amount, currency))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(response.body<PaymentResponse>())
                }
                HttpStatusCode.BadRequest -> {
                    val error = response.body<ErrorResponse>()
                    Result.failure(PaymentException(error.error))
                }
                else -> {
                    Result.failure(PaymentException("Unexpected error: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(PaymentException("Network error: ${e.message}"))
        }
    }


    fun close() {
        client.close()
    }
}


