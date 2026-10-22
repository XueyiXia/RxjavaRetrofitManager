package com.xueyixia.http.config

/**
 * Request payload for one HTTP request.
 */
data class RequestOptions(
    val params: MutableMap<String, Any> = linkedMapOf(),
    val headers: MutableMap<String, String> = linkedMapOf(),
    val tag: String? = null
)
