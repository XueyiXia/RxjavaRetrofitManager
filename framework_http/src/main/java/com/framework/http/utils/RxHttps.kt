package com.framework.http.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.framework.http.Builder
import com.framework.http.RxHttpConfigure
import com.framework.http.enum.HttpMethod
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:41
 * @说明:
 */

open class RxHttps constructor(builder: Builder) {

    private var mContext: Context? = null

    /*请求方式*/
    private var method: HttpMethod? = null

    private var header: TreeMap<String, Any> = TreeMap<String,Any>()

    private var parameter: TreeMap<String, Any> = TreeMap<String,Any>()

    /*LifecycleOwner*/
    private var lifecycleOwner: LifecycleOwner? = null

    /*标识请求的TAG*/
    private var tag: String? = null

    /*文件map*/
    private var fileMap: MutableMap<String, File>? = null

    /*基础URL*/
    private var baseUrl: String? = null

    /*apiUrl*/
    private var apiUrl: String? = null

    /*String参数*/
    private var bodyString: String? = null

    /*是否强制JSON格式*/
    private var isJson = false

    /*超时时长*/
    private var timeout: Long = 0

    /*时间单位*/
    private var timeUnit: TimeUnit? = null





    /**
     * 初始化函数
     */
    init {
        mContext=builder.mContext
        parameter = builder.parameter
        header = builder.header
        tag = builder.tag
        fileMap = builder.fileMap
        baseUrl = builder.baseUrl
        apiUrl = builder.apiUrl
        isJson = builder.isJson
        bodyString = builder.bodyString
        method = builder.method
        timeout = builder.timeout
        timeUnit = builder.timeUnit
        lifecycleOwner = builder.lifecycleOwner
    }


    /**
     * 处理请求头
     */
    private fun disposeHeader(){
        //处理header中文或者换行符出错问题
        for (key in header.keys) {
            header[key]=RequestUtils.getHeaderValueEncoded(header[key])
        }
    }


    /**
     * 处理请求参数
     */
    private fun disposeParameter(){
        //添加基础 Parameter
        RxHttpConfigure.get().getBaseParameter().let {
            if (it.isNotEmpty()) {
                parameter.putAll(it)
            }
        }
    }
}