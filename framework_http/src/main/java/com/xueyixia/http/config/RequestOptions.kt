package com.xueyixia.http.config

/**
 * 请求参数和请求头的统一载体。
 * 业务层只关心“我要请求什么”，不关心每个参数如何塞进底层实现。
 */
data class RequestOptions(
    val params: MutableMap<String, Any> = linkedMapOf(),
    val headers: MutableMap<String, String> = linkedMapOf(),
    val tag: String? = null
)
