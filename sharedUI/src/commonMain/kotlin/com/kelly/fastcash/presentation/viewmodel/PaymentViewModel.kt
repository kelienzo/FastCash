package com.kelly.fastcash.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelly.fastcash.domain.models.PaymentRequest
import com.kelly.fastcash.domain.models.PaymentResponse
import com.kelly.fastcash.domain.usecase.GetTransactionsUseCase
import com.kelly.fastcash.domain.usecase.ProcessPaymentUseCase
import com.kelly.fastcash.domain.usecase.ValidateAmountUseCase
import com.kelly.fastcash.domain.usecase.ValidateCurrencyUseCase
import com.kelly.fastcash.domain.usecase.ValidateEmailUseCase
import com.kelly.fastcash.presentation.ui.screens.PaymentFormEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PaymentViewModel(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _mainPaymentUiState = MutableStateFlow(MainPaymentUiState())
    private val _formUiState = MutableStateFlow(FormUiState())
    private val _paymentUiState = MutableStateFlow(PaymentUiState())

    val transactions = getTransactionsUseCase()

    val mainPaymentUiState =
        combine(_mainPaymentUiState, _formUiState, _paymentUiState) { main, form, payment ->
            main.copy(formUiState = form, paymentUiState = payment)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainPaymentUiState())

    fun onEvent(event: PaymentFormEvent) {
        when (event) {
            is PaymentFormEvent.OnEnterAmount -> updateFormUiState {
                it.copy(amount = event.amount)
            }

            is PaymentFormEvent.OnEnterEmail -> updateFormUiState {
                it.copy(email = event.email)
            }

            is PaymentFormEvent.OnSelectCurrency -> updateFormUiState {
                it.copy(currency = event.currency)
            }

            PaymentFormEvent.OnResetState -> updatePaymentUiState()

            is PaymentFormEvent.OnSendPayment -> sendPayment(
                email = mainPaymentUiState.value.formUiState.email,
                amount = mainPaymentUiState.value.formUiState.amount.toDoubleOrNull() ?: 0.0,
                currency = mainPaymentUiState.value.formUiState.currency
            )

//            else -> {}
        }
    }

    private fun sendPayment(email: String, amount: Double, currency: String) {
        processPaymentUseCase(
            PaymentRequest(
                recipientEmail = email,
                amount = amount,
                currency = currency
            )
        ).onStart {
            updatePaymentUiState(isLoading = true)
        }.onEach { result ->
            result.onSuccess { response ->
                updatePaymentUiState(response = response)
                _formUiState.value = FormUiState()
            }.onFailure { failed ->
                updatePaymentUiState(errorMessage = failed.message)
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFormUiState(update: (FormUiState) -> FormUiState) {
        _formUiState.update(update)
    }

    private fun updatePaymentUiState(
        isLoading: Boolean = false,
        errorMessage: String? = null,
        response: PaymentResponse? = null
    ) {
        _paymentUiState.value = PaymentUiState(
            isLoading, errorMessage, response
        )
    }
}

data class MainPaymentUiState(
    val formUiState: FormUiState = FormUiState(),
    val paymentUiState: PaymentUiState = PaymentUiState()
)

data class FormUiState(
    val email: String = "",
    val amount: String = "",
    val currency: String = ""
)

data class PaymentUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val response: PaymentResponse? = null
)
