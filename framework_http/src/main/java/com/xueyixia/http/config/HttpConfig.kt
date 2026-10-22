package com.xueyixia.http.config

import android.app.Application
import java.util.concurrent.TimeUnit

/**
 * 全局 HTTP 配置中心。
 * 这里不再把请求参数、日志、超时、基础地址散落到各处，统一放在一个入口里。
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

    fun get(): HttpConfig {
        return config ?: throw IllegalStateException(
            "NetworkConfig is not initialized. Call NetworkConfig.initialize(...) first."
        )
    }

    fun getBaseUrl(): String = get().baseUrl

    fun getTimeoutSeconds(): Long = get().timeoutSeconds

    fun getHeaders(): Map<String, String> = get().headers

    fun isLogEnabled(): Boolean = get().enableLog
}
