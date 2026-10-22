package com.xueyixia.http.exception

/**
 * 统一网络异常。
 * 之后的业务层可以只关心 ApiException，不用在各处处理不同的 Throwable。
 */
class ApiException(
    val code: Int = -1,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
