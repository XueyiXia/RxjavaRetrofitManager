package com.framework.http.http

import com.framework.http.BuildConfig
import com.framework.http.utils.HttpConstants
import com.framework.http.utils.SSLUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * 正式版 OkHttpClient 配置：
 * - 生产环境关闭 Body 日志
 * - 统一超时
 * - 仅保留必要 SSL 配置
 */
object OkHttpClientUtils {

    fun getOkHttpClientBuild(): OkHttpClient {
        val loggingLevel = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = loggingLevel
        }

        val builder = OkHttpClient.Builder().apply {
            connectTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            readTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            callTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }

        try {
            val sslParams = SSLUtils.getSSLSocketFactory(ArrayList())
            builder.sslSocketFactory(sslParams.first, sslParams.second)
            builder.hostnameVerifier { _: String, _: SSLSession -> true }
        } catch (_: Exception) {
            // 生产环境下忽略 SSL 初始化异常，保持调用链稳定
        }

        return builder.build()
    }
}
