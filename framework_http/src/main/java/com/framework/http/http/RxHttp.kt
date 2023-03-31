package com.framework.http.http

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.framework.http.api.APIService
import com.framework.http.bean.DownloadInfo
import com.framework.http.config.RxHttpBuilder
import com.framework.http.config.RxHttpConfigure
import com.framework.http.enum.HttpMethod
import com.framework.http.interfac.OnUpLoadFileListener
import com.framework.http.interfac.SimpleResponseListener
import com.framework.http.manager.RetrofitManagerUtils
import com.framework.http.observable.HttpObservable
import com.framework.http.observer.HttpObserver
import com.framework.http.upload.UploadRequestBody
import com.framework.http.utils.HttpConstants
import com.framework.http.utils.Md5Utils
import com.framework.http.utils.RequestUtils
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:41
 * @说明:
 */

open class RxHttp constructor(rxHttpBuilder: RxHttpBuilder) {

    private var mContext: Context? = null

    /*请求方式*/
    private var method: HttpMethod? = null

    private var header: MutableMap<String, Any> = TreeMap<String,Any>()

    private var parameter: MutableMap<String, Any> = TreeMap<String,Any>()

    /*LifecycleOwner*/
    private lateinit var lifecycleOwner: LifecycleOwner

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

    private var httpObserver: HttpObserver<Any>? = null

    private var mSimpleResponseListener: SimpleResponseListener<Any>?=null //网络回调监听

    private var uploadResult: OnUpLoadFileListener<Any>? = null

    var isBreakpoint = false //是否断点下载，true

    /**
     * 初始化函数
     */
    init {
        mContext=rxHttpBuilder.mContext
        parameter = rxHttpBuilder.parameter
        header = rxHttpBuilder.header
        tag = rxHttpBuilder.tag
        fileMap = rxHttpBuilder.fileMap
        baseUrl = rxHttpBuilder.baseUrl
        apiUrl = rxHttpBuilder.apiUrl
        isJson = rxHttpBuilder.isJson
        bodyString = rxHttpBuilder.bodyString
        method = rxHttpBuilder.method
        timeout = rxHttpBuilder.timeout
        timeUnit = rxHttpBuilder.timeUnit
        lifecycleOwner = rxHttpBuilder.lifecycleOwner
    }

    companion object {
        fun getRxHttpBuilder(): RxHttpBuilder {
            return RxHttpBuilder()
        }
    }


    /**
     * 执行网络请求
     * @param simpleResponseListener SimpleResponseListener<T>?
     */
    open fun <T> execute(simpleResponseListener: SimpleResponseListener<T>?) {
        if (simpleResponseListener == null) {
            throw NullPointerException("SimpleResponseListener must not null!")
        } else {
            this.mSimpleResponseListener=simpleResponseListener as SimpleResponseListener<Any>
            doRequest()
        }
    }

    /**
     * 执行普通Http请求 上传文件
     * @param uploadCallback OnUpLoadFileListener<T>?
     */
    open fun <T> execute(uploadCallback: OnUpLoadFileListener<T>?) {
        if (uploadCallback == null) {
            throw java.lang.NullPointerException("UploadCallback must not null!")
        } else {
            uploadResult = uploadCallback as OnUpLoadFileListener<Any>
            doUpload()
        }
    }

    /**
     * 执行请求
     */
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
        httpObserver = HttpObserver(mSimpleResponseListener,tag,lifecycleOwner)

        /**
         * 被观察者和观察者订阅
         */
        val httpObservable = HttpObservable(apiObservable, httpObserver)

        /**
         * 设置监听，被观察和观察者订阅
         */
        httpObservable.observe()

    }


    /**
     * 执行文件上传
     */
    private fun doUpload() {
        /**
         * 请求头处理
         */
        disposeHeader()

        /**
         * 请求参数处理
         */
        disposeParameter()

        /**
         * 处理文件集合
         */
       val fileList = disposeFileUpLoad()

        /**
         * 请求方式处理（被观察）
         */
        val baseUrl= getBaseUrl()!!
        val retrofit=RetrofitManagerUtils.getInstance().getRetrofit(baseUrl)
        val apiService=retrofit.create(APIService::class.java)
        val apiObservable : Observable<Any> = apiService.upload(disposeApiUrl(), parameter, header, fileList) as Observable<Any>

        /**
         * 构造 观察者
         */
        httpObserver = HttpObserver(mSimpleResponseListener,tag,lifecycleOwner)

        /**
         * 被观察者和观察者订阅
         */
        val httpObservable = HttpObservable(apiObservable, httpObserver)

        /**
         * 设置监听，被观察和观察者订阅
         */
        httpObservable.observe()
    }


    private fun doDownload(){
        val file = File("")
//        val downloadInfo = DownloadInfo(apiUrl!!, request.dir, request.filename)
        if (!TextUtils.isEmpty(request.md5)) {
            if (file.exists()) {
                val fileMd5 = Md5Utils.getMD5(file)
//                if (request.md5.equals(fileMd5, ignoreCase = true)) {
//                    downloadInfo.total = file.length()
//                    downloadInfo.progress = file.length()
//                    callback.onNext(downloadInfo as T)
//                    callback.onComplete()
//                    return
//                }
            }
        }

        /**
         * 请求方式处理（被观察）
         */
        val baseUrl= getBaseUrl()!!
        val retrofit=RetrofitManagerUtils.getInstance().getRetrofit(baseUrl)
        val apiService=retrofit.create(APIService::class.java)

        /**
         * 构造 观察者
         */
        httpObserver = HttpObserver(mSimpleResponseListener,tag,lifecycleOwner)

        /**
         * 被观察者和观察者订阅
         */
        val httpObservable = HttpObservable(apiObservable, httpObserver)

        /**
         * 设置监听，被观察和观察者订阅
         */
        httpObservable.observe()

        if (isBreakpoint) {

        } else {

        }
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

        val baseUrl= getBaseUrl()!!
        val retrofit=RetrofitManagerUtils.getInstance().getRetrofit(baseUrl)
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
                apiObservable = if (parameter.isNotEmpty()) {
                    apiService.delete(disposeApiUrl(), parameter, header)
                } else {
                    apiService.delete(disposeApiUrl())
                }

            }

            HttpMethod.PUT -> {
                apiObservable = apiService.put(disposeApiUrl(), parameter, header)
            }

            HttpMethod.HEAD -> {
                apiObservable = apiService.head(disposeApiUrl())

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
        try {
            //添加基础 Parameter
            RxHttpConfigure.get().getBaseParameter()?.let {
                if (it.isNotEmpty()) {
                    parameter.putAll(it)
                }
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

    }


    /**
     * 处理文件集合
     */
    private fun disposeFileUpLoad(): List<MultipartBody.Part>{
        val fileList: MutableList<MultipartBody.Part> = ArrayList()
        fileMap?.let {
            val size = it.size
            var index = 1
            var file: File?
            var requestBody: RequestBody

            for (key in it.keys) {
                file = it[key]
                val mediaType=HttpConstants.MIME_TYPE_MULTIPART_FORM_DATA.toMediaType()
                file?.let {
                    requestBody =file.asRequestBody(mediaType)
                    val uploadRequestBody=UploadRequestBody(requestBody, file, index, size, uploadResult)
                    val part: MultipartBody.Part = MultipartBody.Part.createFormData(key, file.name, uploadRequestBody)
                    fileList.add(part)
                    index++
                }
            }
        }

        return fileList
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


    /**
     * 获取基础URL
     * @return String?
     */
    private fun getBaseUrl()=if(TextUtils.isEmpty(baseUrl)) RxHttpConfigure.get().getBaseUrl() else baseUrl


    /**
     * 获取head 请求长度
     * @param url String
     * @return Long
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getContentLength(url: String): Long {

        return -1
    }
}