package com.kelly.fastcash.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelly.fastcash.domain.models.TransactionsModel
import com.kelly.fastcash.presentation.TextFieldAmountFormat
import com.kelly.fastcash.presentation.viewmodel.MainPaymentUiState
import com.kelly.fastcash.utils.handleAmountInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentFormScreen(
    transactions: List<TransactionsModel>,
    mainPaymentUiState: MainPaymentUiState,
    onEvent: (PaymentFormEvent) -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Send Payment") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            var expanded by rememberSaveable { mutableStateOf(false) }
            val focusManager = LocalFocusManager.current

            OutlinedTextField(
                value = mainPaymentUiState.formUiState.email,
                onValueChange = { onEvent(PaymentFormEvent.OnEnterEmail(it)) },
                label = { Text("Recipient Email") },
                modifier = Modifier.fillMaxWidth()
                    .testTag("email_field")
                    .semantics { contentDescription = "email_field" },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = mainPaymentUiState.formUiState.amount,
                onValueChange = { input ->
                    handleAmountInput(text = input.replace("[-, _]".toRegex(), "")) {
                        onEvent(PaymentFormEvent.OnEnterAmount(it))
                    }
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
                    .testTag("amount_field")
                    .semantics { contentDescription = "amount_field" },
                visualTransformation = TextFieldAmountFormat,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = null)
            )
            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.testTag("currency_selector")
                    .semantics { contentDescription = "currency_selector" }
            ) {
                TextField(
                    placeholder = { Text("Select currency") },
                    modifier = Modifier.fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                    value = mainPaymentUiState.formUiState.currency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("USD", "EUR").forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c) },
                            onClick = {
                                onEvent(PaymentFormEvent.OnSelectCurrency(c))
                                expanded = false
                            },
                            modifier = Modifier.testTag("currency_$c")
                                .semantics { contentDescription = "currency_$c" }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(PaymentFormEvent.OnSendPayment) },
                modifier = Modifier.fillMaxWidth()
                    .testTag("send_button")
                    .semantics { contentDescription = "send_button" },
                enabled = !mainPaymentUiState.paymentUiState.isLoading
            ) {
                if (mainPaymentUiState.paymentUiState.isLoading) CircularProgressIndicator(
//                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                else Text("Send Payment")
            }

//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = { onEvent(PaymentFormEvent.OnNavigateToTransactionHistory) },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//            ) {
//                Text("View Transaction History")
//            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.testTag("transaction_list")
                    .semantics { contentDescription = "transaction_list" },
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (transactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No transactions yet", color = Color.Gray)
                        }
                    }
                } else
                    items(transactions) { transaction ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        transaction.recipientEmail,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = transaction.timestamp,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        "${transaction.currency} ${transaction.amount}",
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        if (transaction.status) "SUCCESS" else "FAILED",
                                        color = if (transaction.status) Color.Green else Color.Red
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    mainPaymentUiState.paymentUiState.run {
        errorMessage?.let { errorMSG ->
            AlertDialog(
                onDismissRequest = { onEvent(PaymentFormEvent.OnResetState) },
                confirmButton = {
                    TextButton(onClick = { onEvent(PaymentFormEvent.OnResetState) }) {
                        Text("OK")
                    }
                },
                title = { Text("Failed") },
                text = { Text(errorMSG) }
            )
        }
        response?.let {
            AlertDialog(
                onDismissRequest = { onEvent(PaymentFormEvent.OnResetState) },
                confirmButton = {
                    TextButton(onClick = { onEvent(PaymentFormEvent.OnResetState) }) {
                        Text("OK")
                    }
                },
                title = { Text("Success") },
                text = { Text("Payment Successful") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentFormScreenPrev() = MaterialTheme {
    PaymentFormScreen(
        transactions = listOf(
            TransactionsModel(
                recipientEmail = "Frank@gmail.com",
                amount = 32424.23,
                currency = "EUR",
                timestamp = "21/06/2026 12:34",
                status = true
            ),
            TransactionsModel(
                recipientEmail = "Frank@gmail.com",
                amount = 32424.23,
                currency = "EUR",
                timestamp = "21/06/2026 11:34",
                status = false
            )
        ),
        mainPaymentUiState = MainPaymentUiState(),
        onEvent = {}
    )
}

sealed interface PaymentFormEvent {
    data object OnResetState : PaymentFormEvent

    //    data object OnNavigateToTransactionHistory : PaymentFormEvent
    data class OnEnterEmail(val email: String) : PaymentFormEvent
    data class OnEnterAmount(val amount: String) : PaymentFormEvent
    data class OnSelectCurrency(val currency: String) : PaymentFormEvent
    data object OnSendPayment : PaymentFormEvent
}
