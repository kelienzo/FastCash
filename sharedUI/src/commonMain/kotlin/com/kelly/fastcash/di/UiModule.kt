package com.kelly.fastcash.di

import com.kelly.fastcash.presentation.viewmodel.PaymentViewModel
import com.kelly.fastcash.presentation.viewmodel.TransactionHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        PaymentViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { TransactionHistoryViewModel(get()) }
}
