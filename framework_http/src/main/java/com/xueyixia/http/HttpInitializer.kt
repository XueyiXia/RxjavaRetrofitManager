package com.xueyixia.http

import android.app.Application
import com.xueyixia.http.config.HttpConfig
import com.xueyixia.http.config.NetworkConfig

/**
 * Public initializer for app startup.
 */
object HttpInitializer {
    fun init(application: Application, config: HttpConfig) {
        NetworkConfig.initialize(application, config)
    }
}
