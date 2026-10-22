package com.xueyixia.http.exception

/**
 * Unified domain-layer exception.
 */
class ApiException(
    val code: Int = -1,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
