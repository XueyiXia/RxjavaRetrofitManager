package com.xueyixia.http.config

import android.app.Application
import java.util.concurrent.TimeUnit

/**
 * Global network configuration.
 * The app should initialize this once at startup.
 */
data class HttpConfig(
    val baseUrl: String,
    val timeoutSeconds: Long = 30L,
    val timeoutUnit: TimeUnit = TimeUnit.SECONDS,
    val enableLog: Boolean = false,
    val headers: Map<String, String> = emptyMap()
)

object NetworkConfig {
    private var config: HttpConfig? = null

    fun initialize(application: Application, config: HttpConfig) {
        this.config = config
    }

    fun get(): HttpConfig = config ?: error(
        "NetworkConfig has not been initialized. Call NetworkConfig.initialize(...) first."
    )

    fun getBaseUrl(): String = get().baseUrl
    fun getTimeoutSeconds(): Long = get().timeoutSeconds
    fun getHeaders(): Map<String, String> = get().headers
    fun isLogEnabled(): Boolean = get().enableLog
}
