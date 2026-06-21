package com.kelly.fastcash.di

import com.kelly.fastcash.data.datasource.DataSource
import com.kelly.fastcash.data.datasource.IosFirestoreDataSource
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        singleOf(::IosFirestoreDataSource) bind DataSource::class
    }