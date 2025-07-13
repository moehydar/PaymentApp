package com.cashi.payment.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cashi.payment.model.Currency
import com.cashi.payment.model.Payment
import com.cashi.payment.model.PaymentStatus
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun EmptyTransactionState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Send your first payment to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCard(
    payment: Payment,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // recipient info
            TransactionInfo(
                payment = payment,
                modifier = Modifier.weight(1f)
            )

            //amount
            TransactionAmount(
                amount = payment.amount,
                currency = payment.currency
            )
        }
    }
}


@Composable
private fun TransactionInfo(
    payment: Payment,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        //recipient email
        Text(
            text = payment.recipientEmail,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        // timestamp
        Text(
            text = formatTimestamp(payment.timestamp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // status badge
        Spacer(modifier = Modifier.height(4.dp))
        StatusBadge(status = payment.status)
    }
}

@Composable
fun StatusBadge(
    status: PaymentStatus,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor) = when (status) {
        PaymentStatus.SUCCESS -> Pair(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        )
        PaymentStatus.FAILED -> Pair(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer
        )
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        modifier = modifier
    ) {
        Text(
            text = status.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}


@Composable
private fun TransactionAmount(
    amount: Double,
    currency: Currency,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        // amount with currency
        Text(
            text = formatAmount(amount, currency),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        //currency code
        Text(
            text = currency.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// format amount to include amount and currency
fun formatAmount(amount: Double, currency: Currency): String {
    val symbol = when (currency) {
        Currency.USD -> "$"
        Currency.EUR -> "€"
    }
    return "$symbol${String.format("%.2f", amount)}"
}

// format timestamp to a readable text
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}