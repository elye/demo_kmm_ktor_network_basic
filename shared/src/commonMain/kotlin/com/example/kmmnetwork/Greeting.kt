package com.example.kmmnetwork

import io.github.aakira.napier.Napier
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Greeting {
    private val httpClient = httpClient {
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
        }
    }.also {
        initLogger()
    }

    suspend fun greeting(): String {
        return "Hello, ${Platform().platform}!\n${getHello()}"
    }

    private suspend fun getHello(): String {
        val response: HttpResponse = httpClient.get("https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=multiplatform")
        return response.readText()
    }
}