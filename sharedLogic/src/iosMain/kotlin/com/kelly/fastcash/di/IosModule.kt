package com.kelly.fastcash.di

import com.kelly.fastcash.data.repository.FirestoreRepositoryImpl
import com.kelly.fastcash.domain.repository.FirestoreRepository
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        singleOf(::FirestoreRepositoryImpl) bind FirestoreRepository::class
    }