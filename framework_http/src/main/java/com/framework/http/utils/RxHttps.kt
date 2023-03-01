package com.framework.http.utils

import java.util.*

/**
 * @author: xiaxueyi
 * @date: 2023-03-01
 * @time: 15:41
 * @说明:
 */

class RxHttps {

    private var header: TreeMap<String, Any> = TreeMap<String,Any>()

    private var parameter: TreeMap<String, Any> = TreeMap<String,Any>()


    /**
     * 处理请求头
     */
    private fun disposeHeader(){
        //处理header中文或者换行符出错问题
        for (key in header.keys) {
            header[key]=RequestUtils.getHeaderValueEncoded(header[key])
        }
    }


    /**
     * 处理请求参数
     */
    private fun disposeParameter(){

    }
}