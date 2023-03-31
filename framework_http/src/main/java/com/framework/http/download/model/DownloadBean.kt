package com.framework.http.download.model


import com.framework.http.api.APIService
import com.framework.http.download.DownloadCallback
import java.io.Serializable

/**
 * 下载实体类
 * 备注:用户使用下载类需要继承此类
 *
 */
class DownloadBean : Serializable {

    constructor() {}
    constructor(url: String?) {
        if (url != null) {
            serverUrl = url
        }
    }

    constructor(url: String?, callback : DownloadCallback<*>?) {
        if (url != null) {
            serverUrl = url
        }
        this.callback = callback
    }



   private var id: Long = 0


    private var localUrl: String? = null//本地存储地址


    private var serverUrl: String =""//下载地址


    private var totalSize: Long = 0//文件大小


    private var currentSize: Long = 0//当前大小


    private var state: State = State.NONE //下载状态


    private var apiService: APIService? = null//接口service


    private var callback: DownloadCallback<*>? = null //回调接口


    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getLocalUrl(): String? {
        return if (localUrl == null) "" else localUrl
    }

    fun setLocalUrl(localUrl: String?) {
        this.localUrl = localUrl
    }

    fun getServerUrl(): String {
        return this.serverUrl
    }

    fun setServerUrl(serverUrl: String) {
        this.serverUrl = serverUrl
    }

    fun getTotalSize(): Long {
        return totalSize
    }

    fun setTotalSize(totalSize: Long) {
        this.totalSize = totalSize
    }

    fun getCurrentSize(): Long {
        return currentSize
    }

    fun setCurrentSize(currentSize: Long) {
        this.currentSize = currentSize
    }

    fun getState(): State? {
        return state
    }

    fun setState(state: State?) {
        this.state = state!!
    }

    fun getApiService(): APIService? {
        return apiService
    }

    fun setApiService(apiService: APIService?) {
        this.apiService = apiService
    }

    fun getCallback(): DownloadCallback<*>? {
        return callback
    }

    fun setCallback(callback: DownloadCallback<*>?) {
        this.callback = callback
    }



    /**
     * 枚举下载状态
     */
    enum class State {
        NONE,  //无状态
        WAITING,  //等待
        LOADING,  //下载中
        PAUSE,  //暂停
        ERROR,  //错误
        FINISH // //完成

    }

    //重置
    fun reset() {
        currentSize = 0
        state = State.NONE
    }
}