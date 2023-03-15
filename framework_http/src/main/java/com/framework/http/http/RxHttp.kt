package com.framework.http.http

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.framework.http.api.APIService
import com.framework.http.enum.HttpMethod
import com.framework.http.function.HttpResultFunction
import com.framework.http.http.config.Builder
import com.framework.http.http.config.RxHttpConfigure
import com.framework.http.interfac.SimpleResponseListener
import com.framework.http.observer.HttpObserver
import com.framework.http.retrofit_manager.RetrofitManagerUtils
import com.framework.http.utils.HttpConstants
import com.framework.http.utils.RequestUtils
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:41
 * @说明:
 */

open class RxHttp constructor(builder: Builder) {

    private var mContext: Context? = null

    /*请求方式*/
    private var method: HttpMethod? = null

    private var header: MutableMap<String, Any> = TreeMap<String,Any>()

    private var parameter: MutableMap<String, Any> = TreeMap<String,Any>()

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

    private var httpObserver: HttpObserver<*>? = null

    private val mSimpleResponseListener: SimpleResponseListener<Any>?=null //网络回调监听


    /**
     * 初始化函数
     */
    init {
        mContext=builder.getContext()
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
     *
     * @param simpleResponseListener SimpleResponseListener<T>?
     */
    open fun <T> execute(simpleResponseListener: SimpleResponseListener<T>?) {
        if (simpleResponseListener == null) {
            throw NullPointerException("SimpleResponseListener must not null!")
        } else {
            doRequest()
        }
    }

    /**
     * 执行请求
     */
    @SuppressLint("CheckResult")
    private fun doRequest() {

        /**
         * 请求头处理
         */
        disposeHeader()

        /**
         * 请求参数处理
         */
        disposeParameter()


        /**
         * 请求方式处理（被观察）
         */
        val apiObservable : Observable<Any> = disposeApiObservable()

        /**
         * 构造 观察者
         */
        httpObserver = HttpObserver(mSimpleResponseListener, lifecycleOwner)

        /**
         * 构造 被观察者
         */
        apiObservable.map(object :io.reactivex.rxjava3.functions.Function<Any,Any>{

            override fun apply(t: Any): Any {
                return t.toString()
            }
        }).onErrorResumeNext(HttpResultFunction<Any>())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(httpObserver as HttpObserver<Any>)

        /**
         * 被观察者和观察者订阅
         */

    }

    /**
     * 接口的被观察者
     * @return Observable<Any>
     */
    private fun disposeApiObservable(): Observable<Any>{
        /**
         * 是否JSON格式提交参数
         */
        val hasBodyString = !TextUtils.isEmpty(bodyString)
        var requestBody: RequestBody? = null
        if (hasBodyString) {
            val mediaType : String = if (isJson) {
                HttpConstants.MIME_TYPE_JSON
            }else{
                HttpConstants.MIME_TYPE_TEXT
            }
            requestBody = bodyString?.toRequestBody(mediaType.toMediaType())
        }

        val retrofit=RetrofitManagerUtils.getInstance().getRetrofit(baseUrl!!)
        val apiService=retrofit.create(APIService::class.java)

        var apiObservable: Observable<JsonElement>? = null
        when (method) {
            HttpMethod.GET ->{
                apiObservable = apiService.get(disposeApiUrl(), parameter, header)
            }

            HttpMethod.POST ->{
                if (hasBodyString){
                    requestBody?.let {
                        apiObservable = apiService.post(disposeApiUrl(), it, header)
                    }
                } else{
                    apiObservable = apiService.post(disposeApiUrl(), parameter, header)
                }
            }

            HttpMethod.DELETE -> {
                apiObservable = apiService.delete(disposeApiUrl(), parameter, header)

            }

            HttpMethod.PUT -> {
                apiObservable = apiService.put(disposeApiUrl(), parameter, header)
            }

            HttpMethod.HEAD -> {
//                apiObservable = apiService.head(disposeApiUrl())
            }

            else -> { //默认get 请求
                apiObservable = apiService.get(disposeApiUrl(), parameter, header)
            }
        }

        return apiObservable as Observable<Any>
    }


    /**
     * 处理请求头
     */
    private fun disposeHeader(){
        //处理header中文或者换行符出错问题
        for (key in header.keys) {
            header[key]= RequestUtils.getHeaderValueEncoded(header[key])
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

    /**
     * 处理接口
     * @return String
     */
    private fun disposeApiUrl(): String {
        if (TextUtils.isEmpty(apiUrl)){
            apiUrl=""
        }
        return apiUrl as String
    }
}