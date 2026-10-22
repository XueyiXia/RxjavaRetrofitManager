package com.xueyixia.http.network

import com.xueyixia.http.config.NetworkConfig
import com.xueyixia.http.utils.HttpConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 负责创建 Retrofit 实例。
 * 这里不负责发起请求，也不负责业务逻辑，职责很清晰。
 */
object RetrofitFactory {

    fun create(baseUrl: String = NetworkConfig.getBaseUrl()): Retrofit {
        val client = OkHttpClientFactory.create(NetworkConfig.isLogEnabled())

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object OkHttpClientFactory {

    fun create(enableLog: Boolean = false): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(HttpConstants.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(HttpConstants.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(HttpConstants.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (enableLog) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }
}
