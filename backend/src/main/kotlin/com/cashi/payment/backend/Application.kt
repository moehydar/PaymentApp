package com.cashi.payment.backend

import com.cashi.payment.model.ErrorResponse
import com.cashi.payment.model.PaymentRequest
import com.cashi.payment.model.PaymentResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import java.util.UUID



fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost() // For development only
    }

    install(CallLogging)

    routing {
        route("/payments") {
            post {
                try {
                    val paymentRequest = call.receive<PaymentRequest>()

                    // Validate the request
                    when {
                        !isValidEmail(paymentRequest.recipientEmail) -> {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("Invalid email format")
                            )
                        }
                        paymentRequest.amount <= 0 -> {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("Amount must be greater than 0")
                            )
                        }
                        paymentRequest.currency !in listOf("USD", "EUR") -> {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ErrorResponse("Unsupported currency. Use USD or EUR")
                            )
                        }
                        else -> {
                            // Process the payment (mock processing)
                            val paymentResponse = PaymentResponse(
                                id = "payment_${UUID.randomUUID()}",
                                recipientEmail = paymentRequest.recipientEmail,
                                amount = paymentRequest.amount,
                                currency = paymentRequest.currency,
                                status = "success",
                                timestamp = Clock.System.now().toEpochMilliseconds()
                            )

                            call.respond(HttpStatusCode.OK, paymentResponse)
                        }
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse("Internal server error: ${e.message}")
                    )
                }
            }
        }

        // Health check endpoint
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
        }
    }
}
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return email.matches(emailRegex)
}
