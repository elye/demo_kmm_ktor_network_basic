package com.example.kmmnetwork

import io.github.aakira.napier.Napier
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType.Application.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Greeting {

    object Model {
        @Serializable
        data class Result(val query: Query)
        @Serializable
        data class Query(val searchinfo: SearchInfo)
        @Serializable
        data class SearchInfo(val totalhits: Int)
    }

    private val httpClient = httpClient {
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
        }
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                // This issue is temporarily fix as per https://stackoverflow.com/q/67969562/3286489
                useAlternativeNames = false
            }
            serializer = KotlinxSerializer(json)
        }
    }.also {
        initLogger()
    }

    @Throws(Exception::class)
    suspend fun greeting(): String {
        return "Hello, ${Platform().platform}!\n${getHello()}"
    }

    private suspend fun getHello(): Model.Result {
        return httpClient.get("https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=multiplatform")
    }
}