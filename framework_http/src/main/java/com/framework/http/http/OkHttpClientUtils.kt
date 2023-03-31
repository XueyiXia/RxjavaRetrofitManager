package com.framework.http.http

import com.framework.http.utils.HttpConstants
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

    fun getOkHttpClientBuild(): OkHttpClient {

        //设置日志等级
        val httpLoggingInterceptor = HttpLoggingInterceptor();
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}