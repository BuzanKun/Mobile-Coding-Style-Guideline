package com.example.shared.di

import com.example.shared.util.Constant
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

expect fun platformHttpEngine(): HttpClientEngine

val networkModule = module {
    single {
        HttpClient(platformHttpEngine()) {
            // Logging Plugin
            install(Logging) {
                level = LogLevel.ALL
            }
            // JSON Serialization Plugin
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            // Default Request Plugin
            install(DefaultRequest) {
                url(Constant.BASE_URL)
            }
        }
    }
}