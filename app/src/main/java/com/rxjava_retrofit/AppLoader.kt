package com.rxjava_retrofit

import android.app.Application
import com.framework.http.http.RxHttp
import com.framework.http.http.config.Builder
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:46
 * @说明:
 */

class AppLoader :Application() {




    override fun onCreate() {
        super.onCreate()
    }


    /**
     * 初始化网络请求
     */
    init {
        initRHttp();
    }

    /**
     * 初始化网络请求
     */
    private fun initRHttp() {
        val headerMap :MutableMap<String, Any> = mutableMapOf()
        headerMap["Content-Type"] = "application/x-www-form-urlencoded" //默认的编码方式
        headerMap["Connection"] = "Keep-Alive"
        headerMap["Accept-Language"] = "zh-cn"
        headerMap["Accept"] = "Application/Json"

        //必须初始化

        val builder: Builder=Builder()

        builder.baseUrl(HttpApi.BASE_URL)                   //基础URL
        builder.build()                    //初始化
        RxHttp(builder)
    }
}