package com.xueyixia.http.callback

import com.xueyixia.http.exception.ApiException

/**
 * 通用请求回调，适合普通 JSON 请求。
 */
interface ApiCallback<T> {
    fun onSuccess(data: T)
    fun onError(error: ApiException)
}
