package com.framework.http

import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */

class RxHttpConfig {
     val TIME_OUT = 60L
    private object Config {
        val holder = RxHttpConfig()
    }


    var okhttpBuilder: OkHttpClient.Builder? = null
    var maxRetries = 0
    var retryDelayMillis = 0L
    var certificates:MutableList<InputStream>?=null


    /**
     * 设置默认的
     * @return OkHttpClient.Builder
     */
    private fun defaultBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder().apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        }
//        try {
//            val sslParams= SSLUtil.getSSLSocketFactory(certificates =certificates)
//            builder.sslSocketFactory(sslParams.first,sslParams.second)
//            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyManagementException) {
//            e.printStackTrace()
//        }
        return builder
    }
}