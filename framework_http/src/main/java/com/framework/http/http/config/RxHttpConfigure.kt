package com.framework.http.http.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import java.util.TreeMap
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:57
 * @说明:
 */

class RxHttpConfigure {

    @SuppressLint("StaticFieldLeak")
    private object Config {
        val rxHttpConfigure = RxHttpConfigure()
    }
    companion object {
        fun get(): RxHttpConfigure {
            return Config.rxHttpConfigure
        }
    }

    /*全局上下文*/
    private var context: Context? = null

    /*请求基础路径*/
    private var baseUrl: String? = null

    /*超时时长*/
    private var timeout: Long = 60

    /*时间单位*/
    private lateinit var timeUnit: TimeUnit

    /*Handler*/
    /*全局Handler*/
    private var handler: Handler? = null

    /*请求参数*/
    private var baseParameter: TreeMap<String, Any>? = null

    /*header*/
    private var header: TreeMap<String, Any>? = null

    /*是否显示Log*/
    private var isShowLog: Boolean = false

    fun init(app: Application): RxHttpConfigure {
        context = app
        handler = Handler(Looper.getMainLooper())
        return this
    }


    /**
     *
     * @param baseUrl String?
     * @return RxHttpConfigure
     */
    fun baseUrl(baseUrl: String?): RxHttpConfigure {
        this.baseUrl = baseUrl
        return this
    }

    fun getBaseUrl(): String ?{
        return baseUrl
    }

    /**
     * 设置基础参数
     */
    fun setBaseParameter(parameter: TreeMap<String, Any>?): RxHttpConfigure {
        baseParameter = parameter
        return this
    }

    fun getBaseParameter():TreeMap<String, Any>{
        return baseParameter!!
    }

    /*基础Header*/
    fun baseHeader(header: TreeMap<String, Any>?): RxHttpConfigure {
        this.header = header
        return this
    }

    val baseHeader: MutableMap<String, Any>
        get() = if (header == null) TreeMap() else header!!

    /*超时时长*/
    fun timeout(timeout: Long): RxHttpConfigure {
        this.timeout = timeout
        return this
    }

    /*是否显示LOG*/
    fun showLog(showLog: Boolean): RxHttpConfigure {
        isShowLog = showLog
        return this
    }

    /*时间单位*/
    fun timeUnit(timeUnit: TimeUnit): RxHttpConfigure {
        this.timeUnit = timeUnit
        return this
    }
}