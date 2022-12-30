package com.framework.http.interfac

/**
 * @author: xiaxueyi
 * @date: 2022-08-18
 * @time: 14:45
 * @说明: 请求参数回调接口
 */
interface NetworkRequestParamsListener {

    fun getHeaderParams():MutableMap<String,Any>
}