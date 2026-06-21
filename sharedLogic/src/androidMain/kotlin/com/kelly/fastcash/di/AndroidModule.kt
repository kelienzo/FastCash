package com.kelly.fastcash.di

import com.google.firebase.firestore.FirebaseFirestore
import com.kelly.fastcash.data.datasource.DataSource
import com.kelly.fastcash.data.datasource.AndroidFirestoreDataSource
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { FirebaseFirestore.getInstance() }
        singleOf(::AndroidFirestoreDataSource) bind DataSource::class
    }