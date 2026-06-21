package com.kelly.fastcash.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object PaymentFormRoute : Routes

    @Serializable
    data object TransactionHistoryRoute : Routes
}