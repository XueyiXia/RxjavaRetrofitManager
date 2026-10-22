package com.xueyixia.http.core

/**
 * HTTP 方法枚举。
 * 只保留核心请求方式，避免在代码里到处出现魔法字符串。
 */
enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD
}
