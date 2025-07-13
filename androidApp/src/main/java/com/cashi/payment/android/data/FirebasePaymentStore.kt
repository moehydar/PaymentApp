package com.cashi.payment.android.data

import com.cashi.payment.model.Currency
import com.cashi.payment.model.Payment
import com.cashi.payment.model.PaymentStatus
import com.cashi.payment.repository.PaymentStore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebasePaymentStore : PaymentStore {

    private val firestore = FirebaseFirestore.getInstance()
    private val paymentsCollection = firestore.collection("payments")

    //  saves a payment to the Firestore database.
    override suspend fun savePayment(payment: Payment) {
        val paymentMap = hashMapOf(
            "id" to payment.id,
            "recipientEmail" to payment.recipientEmail,
            "amount" to payment.amount,
            "currency" to payment.currency.name,
            "status" to payment.status.name,
            "timestamp" to payment.timestamp
        )

        paymentsCollection.document(payment.id)
            .set(paymentMap)
            .await()
    }

    //retrieves all payments from the Firestore database, ordered by timestamp
    override suspend fun getAllPayments(): List<Payment> {
        val snapshot = paymentsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            try {
                Payment(
                    id = doc.getString("id") ?: return@mapNotNull null,
                    recipientEmail = doc.getString("recipientEmail") ?: return@mapNotNull null,
                    amount = doc.getDouble("amount") ?: return@mapNotNull null,
                    currency = Currency.valueOf(doc.getString("currency") ?: return@mapNotNull null),
                    status = PaymentStatus.valueOf(doc.getString("status") ?: return@mapNotNull null),
                    timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    // observe payments that are added during the app run, in order to update the UI in real-time.
    override fun observePayments(): Flow<List<Payment>> = callbackFlow {
        val listenerRegistration = paymentsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val payments = snapshot.documents.mapNotNull { doc ->
                        try {
                            Payment(
                                id = doc.getString("id") ?: return@mapNotNull null,
                                recipientEmail = doc.getString("recipientEmail") ?: return@mapNotNull null,
                                amount = doc.getDouble("amount") ?: return@mapNotNull null,
                                currency = Currency.valueOf(doc.getString("currency") ?: return@mapNotNull null),
                                status = PaymentStatus.valueOf(doc.getString("status") ?: return@mapNotNull null),
                                timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(payments)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}