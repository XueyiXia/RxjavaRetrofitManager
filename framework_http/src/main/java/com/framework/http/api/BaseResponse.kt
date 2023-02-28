package com.framework.http.api

import java.io.Serializable

class BaseResponse<D> : Serializable {
    private var isSuccess = false
    private var code = 0
    private var message: String? = null
    private var data: D? = null

    fun setData(data: D) {
        this.data = data
    }
}