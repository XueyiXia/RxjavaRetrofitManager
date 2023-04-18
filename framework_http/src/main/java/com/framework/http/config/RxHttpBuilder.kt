package com.framework.http.config

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.framework.http.enum.HttpMethod
import com.framework.http.http.RxHttp
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-07
 * @time: 11:04
 * @说明: 构建网络请求所需的参数
 */

class RxHttpBuilder {

    var mContext: Context? = null

    /*请求方式*/
    var method: HttpMethod? = null

    var header: MutableMap<String, Any> = TreeMap<String,Any>()

    var parameter: MutableMap<String, Any> = TreeMap<String,Any>()

    /*LifecycleOwner*/
    var lifecycleOwner: LifecycleOwner?=null

    /*标识请求的TAG*/
    var tag: String? = null

    /*文件map*/
    var fileMap: MutableMap<String, File>? = null

    /*基础URL*/
    var baseUrl: String? = null

    /*apiUrl*/
    var apiUrl: String? = null

    /*String参数*/
    var bodyString: String? = null

    /*是否强制JSON格式*/
    var isJson = false

    /*超时时长*/
    var timeout: Long = 0

    /*时间单位*/
    var timeUnit: TimeUnit? = null

    var downloadConfigure:DownloadConfigure?=null


    /**
     * 设置上下文对象
     * @param context Context?
     * @return Builder
     */
    fun setContext(context: Context): RxHttpBuilder {
        mContext = context
        return this
    }
    fun getContext(): Context {
        return mContext!!
    }


    /**
     * 设置请求方式
     * @param method HttpMethod
     * @return Builder
     */
    fun setMethod(method: HttpMethod): RxHttpBuilder {
        this.method=method;
        return this;
    }


    /**
     * GET 请求
     * @return Builder
     */
    fun get(): RxHttpBuilder {
        this.method=HttpMethod.GET
        return  this;
    }

    /**
     * POST请求
     * @return Builder
     */
    fun post(): RxHttpBuilder {
        this.method=HttpMethod.POST;
        return this
    }

    /**
     * DELETE 请求
     * @return Builder
     */
    fun delete(): RxHttpBuilder {
        method = HttpMethod.DELETE
        return this
    }

    /**
     * PUT 请求
     * @return Builder
     */
    fun put(): RxHttpBuilder {
        method = HttpMethod.PUT
        return this
    }

    /**
     * Head 请求
     * @return Builder
     */
    fun head(): RxHttpBuilder {
        method = HttpMethod.HEAD
        return this
    }

    /**
     * 基础URL
     * @param baseUrl String?
     * @return Builder
     */
    fun baseUrl(baseUrl: String?): RxHttpBuilder {
        this.baseUrl = baseUrl
        return this
    }


    /**
     * API URL
     * @param apiUrl String?
     * @return Builder
     */
    fun setApiUrl(apiUrl: String?): RxHttpBuilder {
        this.apiUrl = apiUrl
        return this
    }

    /**
     * 增加 Parameter 不断叠加参数 包括基础参数
     * @param parameter TreeMap<String, Any>?
     * @return Builder
     */
    fun addParameter(parameter: TreeMap<String, Any>?): RxHttpBuilder {
        if (this.parameter == null) {
            this.parameter = TreeMap()
        }
        this.parameter!!.putAll((parameter)!!)
        return this
    }




    /**
     * 设置参数 ,设置 Parameter 会覆盖 Parameter 包括基础参数
     * @param parameter TreeMap<String, Any>
     * @return Builder
     */
    fun setParameter(parameter: TreeMap<String, Any>): RxHttpBuilder {
        this.parameter=parameter;
        return this
    }

    /**
     * 设置String 类型参数  覆盖之前设置  isJson:是否强制JSON格式    bodyString设置后Parameter则无效
     * @param bodyString String?
     * @param isJson Boolean
     * @return Builder
     */
    fun setBodyString(bodyString: String?, isJson: Boolean): RxHttpBuilder {
        this.isJson = isJson
        this.bodyString = bodyString
        return this
    }

    /**
     * 增加 Header 不断叠加 Header 包括基础 Header
     * @param header TreeMap<String, Any>?
     * @return Builder
     */
    fun addHeader(header: TreeMap<String, Any>?): RxHttpBuilder {
        if (this.header == null) {
            this.header = sortedMapOf()
        }
        this.header!!.putAll((header)!!)
        return this
    }

    /**
     * 设置 Header 会覆盖 Header 包括基础参数
     * @param header TreeMap<String, Any>?
     * @return Builder
     */
    fun setHeader(header: MutableMap<String, Any>?): RxHttpBuilder {
        this.header = header!!
        return this
    }

    /**
     * LifecycleProvider
     * @param lifecycleOwner LifecycleOwner?
     * @return Builder
     */
    fun setLifecycle(lifecycleOwner: LifecycleOwner): RxHttpBuilder {
        this.lifecycleOwner = lifecycleOwner
        return this
    }

    /**
     * tag
     * @param tag String?
     * @return Builder
     */
    fun tag(tag: String?): RxHttpBuilder {
        this.tag = tag
        return this
    }

    /**
     * 文件集合
     * @param file TreeMap<String, File>?
     * @return Builder
     */
    fun file(file: TreeMap<String, File>?): RxHttpBuilder {
        fileMap = file
        return this
    }

    /**
     * 一个Key对应多个文件
     * @param key String
     * @param fileList List<File>
     * @return Builder
     */
    fun file(key: String, fileList: List<File>): RxHttpBuilder {
        if (fileMap == null) {
            fileMap = IdentityHashMap()
        }
        if (fileList.isNotEmpty()) {
            for (file: File in fileList) {
                fileMap!![key] = file
            }
        }
        return this
    }

    /**
     * 超时时长
     * @param timeout Long
     * @return Builder
     */
    fun timeout(timeout: Long): RxHttpBuilder {
        this.timeout = timeout
        return this
    }

    /**
     * 时间单位
     * @param timeUnit TimeUnit?
     * @return Builder
     */
    fun timeUnit(timeUnit: TimeUnit?): RxHttpBuilder {
        this.timeUnit = timeUnit
        return this
    }

    /**
     *
     * @param downloadConfigure DownloadConfigure
     */
    fun setDownloadConfigure(downloadConfigure: DownloadConfigure): RxHttpBuilder{
        this.downloadConfigure=downloadConfigure
        return this
    }


    fun build(): RxHttp {
        return RxHttp(this)
    }
}