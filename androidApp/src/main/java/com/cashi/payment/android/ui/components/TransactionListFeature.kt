package com.cashi.payment.android.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cashi.payment.model.Payment
import com.cashi.payment.model.PaymentStatus


@Composable
fun TransactionSummaryCard(
    totalAmount: Double,
    transactionCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total Sent",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = transactionCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Transactions",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// a nice feature to filter the transactions by status, although status is now always SUCCESS :)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilterChips(
    selectedStatus: PaymentStatus?,
    onStatusSelected: (PaymentStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("All") }
        )

        PaymentStatus.entries.forEach { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = { Text(status.name.lowercase().replaceFirstChar { it.uppercase() }) },
                leadingIcon = {
                    Icon(
                        imageVector = getStatusIcon(status),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

private fun getStatusIcon(status: PaymentStatus): ImageVector = when (status) {
    PaymentStatus.SUCCESS -> Icons.Default.Check
    PaymentStatus.FAILED -> Icons.Default.Close
}
