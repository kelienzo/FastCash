package com.kelly.fastcash.di

import com.kelly.fastcash.data.remote.PaymentService
import com.kelly.fastcash.data.repository.PaymentRepositoryImpl
import com.kelly.fastcash.domain.repository.PaymentRepository
import com.kelly.fastcash.domain.usecase.GetTransactionsUseCase
import com.kelly.fastcash.domain.usecase.ProcessPaymentUseCase
import com.kelly.fastcash.domain.usecase.ValidateAmountUseCase
import com.kelly.fastcash.domain.usecase.ValidateCurrencyUseCase
import com.kelly.fastcash.domain.usecase.ValidateEmailUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration(this)
        modules(sharedModule, platformModule)
    }

// called by iOS etc
fun initKoin() = initKoin {}

expect val platformModule: Module

val sharedModule = module {
    single {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
    single { PaymentService(get()) }
    singleOf(::PaymentRepositoryImpl) bind PaymentRepository::class
    factory { ProcessPaymentUseCase(get(), get()) }
    factory { GetTransactionsUseCase(get()) }
    factory { ValidateEmailUseCase() }
    factory { ValidateAmountUseCase() }
    factory { ValidateCurrencyUseCase() }
}
