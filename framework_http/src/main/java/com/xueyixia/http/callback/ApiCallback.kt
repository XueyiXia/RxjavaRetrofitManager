package com.xueyixia.http.callback

import com.xueyixia.http.exception.ApiException

/**
 * Normal JSON request callback.
 */
interface ApiCallback<T> {
    fun onSuccess(data: T)
    fun onError(error: ApiException)
}
