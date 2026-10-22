package com.xueyixia.http.callback

import com.xueyixia.http.exception.ApiException

interface UploadCallback {
    fun onProgress(current: Long, total: Long, percentage: Int)
    fun onSuccess()
    fun onError(error: ApiException)
}
