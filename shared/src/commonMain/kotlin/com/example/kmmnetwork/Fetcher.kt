package com.example.kmmnetwork

import io.github.aakira.napier.Napier
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class Fetcher {

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
    suspend fun fetch(keyword: String): String {
        return "Hello, ${Platform().platform}!\n${getHitCount(keyword)}"
    }

    private suspend fun getHitCount(keyword: String): Model.Result {
        return httpClient.submitForm(
            url = "https://en.wikipedia.org/w/api.php",
            formParameters = Parameters.build {
                append("action", "query")
                append("format", "json")
                append("list", "search")
                append("srsearch", keyword)
            },
            encodeInQuery = true
        )
    }
}
