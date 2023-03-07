package com.framework.http.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 15:56
 * @说明:
 */

object OkHttpClientUtils {

    private const val TAG="OkHttpClientUtils"

    private const val mTimeOut: Long=60

    fun getOkHttpClientBuild(): OkHttpClient {

        //设置日志等级
        val httpLoggingInterceptor = HttpLoggingInterceptor();
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(mTimeOut, TimeUnit.SECONDS)
            .writeTimeout(mTimeOut, TimeUnit.SECONDS)
            .connectTimeout(mTimeOut, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}