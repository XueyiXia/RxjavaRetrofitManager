package com.framework.http.exception


/**
 * @author: xiaxueyi
 * @date: 2022-08-18
 * @time: 15:28
 * @说明: 整个事件链错误类型
 */
class EventException : Exception {
    var code: Any //错误码

    var msg: String//错误信息

    /**
     *
     */
    constructor(code: Any, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(throwable: Throwable?, code: Any, msg: String) : super(throwable) {
        this.code = code
        this.msg = msg
    }
}