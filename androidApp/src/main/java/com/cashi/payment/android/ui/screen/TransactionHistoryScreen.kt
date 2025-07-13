package com.cashi.payment.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashi.payment.android.ui.components.*
import com.cashi.payment.model.Payment
import com.cashi.payment.model.PaymentStatus
import org.koin.androidx.compose.koinViewModel
import com.cashi.payment.android.viewmodel.TransactionHistoryViewModel


@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel = koinViewModel()
) {
    val payments by viewModel.payments.collectAsState()

    // local state for filtering
    var selectedStatus by remember { mutableStateOf<PaymentStatus?>(null) }

    // filter payments based on selected status
    val filteredPayments = remember(payments, selectedStatus) {
        if (selectedStatus == null) {
            payments
        } else {
            payments.filter { it.status == selectedStatus }
        }
    }

    //calculate summary data
    val totalAmount = remember(filteredPayments) {
        filteredPayments.sumOf { it.amount }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TransactionHistoryHeader()

        // summary Card (only show if there are transactions)
        if (payments.isNotEmpty()) {
            TransactionSummaryCard(
                totalAmount = totalAmount,
                transactionCount = filteredPayments.size,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //filter
            TransactionFilterChips(
                selectedStatus = selectedStatus,
                onStatusSelected = { status -> selectedStatus = status },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        //list or empty state
        if (filteredPayments.isEmpty()) {
            if (selectedStatus != null && payments.isNotEmpty()) {
                // No results for filter
                EmptyFilterState(selectedStatus = selectedStatus!!)
            } else {
                // No transactions at all
                EmptyTransactionState()
            }
        } else {
            TransactionList(payments = filteredPayments)
        }
    }
}

@Composable
private fun TransactionHistoryHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Transaction History",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun TransactionList(
    payments: List<Payment>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(
            items = payments,
            key = { it.id }
        ) { payment ->
            TransactionCard(
                payment = payment,
                onClick = {
                    // for now we won't do anything on click, if there was more time i would have implemented a detail screen
                }
            )
        }
    }
}

@Composable
private fun EmptyFilterState(
    selectedStatus: PaymentStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No ${selectedStatus.name.lowercase()} transactions",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Try selecting a different filter",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}