package com.xueyixia.http.callback

import com.xueyixia.http.exception.ApiException

interface DownloadCallback<T> {
    fun onStart()
    fun onProgress(current: Long, total: Long, percentage: Int)
    fun onSuccess(data: T)
    fun onError(error: ApiException)
}
