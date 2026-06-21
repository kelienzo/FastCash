package com.kelly.fastcash.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelly.fastcash.presentation.ui.screens.PaymentFormEvent
import com.kelly.fastcash.presentation.ui.screens.PaymentFormScreen
import com.kelly.fastcash.presentation.ui.screens.TransactionHistoryScreen
import com.kelly.fastcash.presentation.viewmodel.PaymentViewModel
import com.kelly.fastcash.presentation.viewmodel.TransactionHistoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FastCashNavGraph(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.PaymentFormRoute
    ) {
        composable<Routes.PaymentFormRoute> {
            val viewModel = koinViewModel<PaymentViewModel>()
            val mainPaymentUiState by viewModel.mainPaymentUiState.collectAsStateWithLifecycle()

            PaymentFormScreen(
                mainPaymentUiState = mainPaymentUiState,
                onEvent = { event ->
                    when(event) {
                        PaymentFormEvent.OnNavigateToTransactionHistory -> navController.navigate(Routes.TransactionHistoryRoute)
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }

        composable<Routes.TransactionHistoryRoute> {
            val viewModel = koinViewModel<TransactionHistoryViewModel>()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle(emptyList())

            TransactionHistoryScreen(
                transactions = transactions,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
