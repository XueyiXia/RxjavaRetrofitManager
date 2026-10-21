package com.framework.http.api

import java.io.Serializable

/**
 * 统一服务端通用返回结构。
 * 对外仍保持兼容原有 getter/setter 形式，便于历史代码直接迁移。
 */
class BaseResponse<T> : Serializable {
    private var isSuccess = false
    private var code: Any = 0
    private var message: String? = null
    private var data: T? = null

    fun getMessage(): String {
        return message ?: ""
    }

    fun setMessage(msg: String) {
        message = msg
    }

    fun getCode(): Any {
        return code
    }

    fun setCode(code: Any) {
        this.code = code
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T?) {
        this.data = data
    }

    fun isSuccess(): Boolean {
        return isSuccess
    }

    fun setSuccess(success: Boolean) {
        isSuccess = success
    }
}
