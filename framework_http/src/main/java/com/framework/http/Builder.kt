package com.framework.http

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.framework.http.enum.HttpMethod
import com.framework.http.utils.RxHttps
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-07
 * @time: 11:04
 * @说明:
 */

class Builder {

    private var mContext: Context? = null

    /*请求方式*/
    private var method: HttpMethod? = null

    /*请求参数*/
    private var parameter: MutableMap<String, Any>? = null

    /*header*/
    private var header: MutableMap<String, Any>? = null

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
     * 设置上下文对象
     * @param context Context?
     * @return Builder
     */
    fun setContext(context: Context?):Builder {
        mContext = context
        return this
    }


    /**
     * 设置请求方式
     * @param method HttpMethod
     * @return Builder
     */
    fun setMethod(method: HttpMethod):Builder{
        this.method=method;
        return this;
    }


    /**
     * GET 请求
     * @return Builder
     */
    fun get():Builder{
        this.method=HttpMethod.GET
        return  this;
    }

    /**
     * POST请求
     * @return Builder
     */
    fun post():Builder{
        this.method=HttpMethod.POST;
        return this
    }

    /**
     * DELETE 请求
     * @return Builder
     */
    fun delete(): Builder {
        method = HttpMethod.DELETE
        return this
    }

    /**
     * PUT 请求
     * @return Builder
     */
    fun put(): Builder {
        method = HttpMethod.PUT
        return this
    }

    /**
     * Head 请求
     * @return Builder
     */
    fun head(): Builder {
        method = HttpMethod.HEAD
        return this
    }

    /**
     * 基础URL
     * @param baseUrl String?
     * @return Builder
     */
    fun baseUrl(baseUrl: String?): Builder {
        this.baseUrl = baseUrl
        return this
    }

    /**
     * API URL
     * @param apiUrl String?
     * @return Builder
     */
    fun setApiUrl(apiUrl: String?): Builder {
        this.apiUrl = apiUrl
        return this
    }

    /**
     * 增加 Parameter 不断叠加参数 包括基础参数
     * @param parameter TreeMap<String, Any>?
     * @return Builder
     */
    fun addParameter(parameter: TreeMap<String, Any>?): Builder {
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
    fun setParameter(parameter: TreeMap<String, Any>):Builder{
        this.parameter=parameter;
        return this
    }

    /**
     * 设置String 类型参数  覆盖之前设置  isJson:是否强制JSON格式    bodyString设置后Parameter则无效
     * @param bodyString String?
     * @param isJson Boolean
     * @return Builder
     */
    fun setBodyString(bodyString: String?, isJson: Boolean): Builder {
        this.isJson = isJson
        this.bodyString = bodyString
        return this
    }

    /**
     * 增加 Header 不断叠加 Header 包括基础 Header
     * @param header TreeMap<String, Any>?
     * @return Builder
     */
    fun addHeader(header: TreeMap<String, Any>?): Builder {
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
    fun setHeader(header: TreeMap<String, Any>?): Builder {
        this.header = header
        return this
    }

    /**
     * LifecycleProvider
     * @param lifecycleOwner LifecycleOwner?
     * @return Builder
     */
    fun setLifecycle(lifecycleOwner: LifecycleOwner?): Builder {
        this.lifecycleOwner = lifecycleOwner
        return this
    }

    /**
     * tag
     * @param tag String?
     * @return Builder
     */
    fun tag(tag: String?): Builder {
        this.tag = tag
        return this
    }

    /**
     * 文件集合
     * @param file TreeMap<String, File>?
     * @return Builder
     */
    fun file(file: TreeMap<String, File>?): Builder {
        fileMap = file
        return this
    }

    /**
     * 一个Key对应多个文件
     * @param key String
     * @param fileList List<File>
     * @return Builder
     */
    fun file(key: String, fileList: List<File>): Builder {
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
    fun timeout(timeout: Long): Builder {
        this.timeout = timeout
        return this
    }

    /**
     * 时间单位
     * @param timeUnit TimeUnit?
     * @return Builder
     */
    fun timeUnit(timeUnit: TimeUnit?): Builder {
        this.timeUnit = timeUnit
        return this
    }


    fun build(): RxHttps {
        return RxHttps(this)
    }
}