package com.cashi.payment.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashi.payment.android.data.PaymentUiState
import com.cashi.payment.android.ui.components.*
import com.cashi.payment.android.viewmodel.PaymentViewModel
import com.cashi.payment.model.Currency
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        PaymentScreenHeader()

        //fields
        PaymentFormSection(
            uiState = uiState,
            onEmailChange = viewModel::updateEmail,
            onAmountChange = viewModel::updateAmount,
            onCurrencyChange = viewModel::updateCurrency,
            currencyDropdownExpanded = currencyDropdownExpanded,
            onCurrencyDropdownExpandedChange = { currencyDropdownExpanded = it }
        )

        //submit
        PaymentButton(
            onClick = viewModel::sendPayment,
            enabled = uiState.recipientEmail.isNotEmpty() &&
                    uiState.amount.isNotEmpty(),
            isLoading = uiState.isLoading
        )

        //status message
        PaymentMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage
        )
    }
}

@Composable
private fun PaymentScreenHeader() {
    Text(
        text = "Send Payments",
        style = MaterialTheme.typography.headlineMedium
    )

    // TODO: maybe later we can add the balace that the user has in their account
}

@Composable
private fun PaymentFormSection(
    uiState: PaymentUiState,
    onEmailChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCurrencyChange: (Currency) -> Unit,
    currencyDropdownExpanded: Boolean,
    onCurrencyDropdownExpandedChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        EmailInputField(
            value = uiState.recipientEmail,
            onValueChange = onEmailChange,
            error = uiState.emailError
        )

        AmountInputField(
            value = uiState.amount,
            onValueChange = onAmountChange,
            currency = uiState.selectedCurrency,
            error = uiState.amountError
        )

        CurrencySelector(
            selectedCurrency = uiState.selectedCurrency,
            onCurrencySelected = onCurrencyChange,
            expanded = currencyDropdownExpanded,
            onExpandedChange = onCurrencyDropdownExpandedChange
        )
    }
}

@Composable
private fun PaymentMessages(
    errorMessage: String?,
    successMessage: String?
) {
    // show error message if present
    errorMessage?.let { message ->
        MessageCard(
            message = message,
            isError = true
        )
    }

    // show success message if present
    successMessage?.let { message ->
        MessageCard(
            message = message,
            isError = false
        )
    }
}