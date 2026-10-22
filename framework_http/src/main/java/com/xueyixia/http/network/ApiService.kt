package com.xueyixia.http.network

import com.xueyixia.http.config.RequestOptions
import com.xueyixia.http.core.HttpMethod
import retrofit2.Call
import retrofit2.http.*

/**
 * Retrofit API definition.
 */
interface ApiService {

    @GET
    fun get(
        @Url url: String,
        @QueryMap params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>
    ): Call<String>

    @FormUrlEncoded
    @POST
    fun post(
        @Url url: String,
        @FieldMap params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>
    ): Call<String>

    @POST
    fun postJson(
        @Url url: String,
        @Body body: String,
        @HeaderMap headers: Map<String, String>
    ): Call<String>

    @DELETE
    fun delete(
        @Url url: String,
        @QueryMap params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>
    ): Call<String>

    @PUT
    fun put(
        @Url url: String,
        @FieldMap params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>
    ): Call<String>

    @HEAD
    fun head(@Url url: String): Call<Void>
}

class HttpRequestBuilder(
    private val baseUrl: String
) {
    private val params = linkedMapOf<String, Any>()
    private val headers = linkedMapOf<String, String>()
    private var method: HttpMethod = HttpMethod.GET
    private var path: String = ""
    private var body: String? = null
    private var tag: String? = null

    fun method(method: HttpMethod): HttpRequestBuilder = apply { this.method = method }
    fun path(path: String): HttpRequestBuilder = apply { this.path = path }
    fun addParam(key: String, value: Any): HttpRequestBuilder = apply { params[key] = value }
    fun addHeader(key: String, value: String): HttpRequestBuilder = apply { headers[key] = value }
    fun body(body: String): HttpRequestBuilder = apply { this.body = body }
    fun tag(tag: String): HttpRequestBuilder = apply { this.tag = tag }

    fun build(): RequestOptions {
        return RequestOptions(
            params = params,
            headers = headers,
            tag = tag
        )
    }

    fun getMethod(): HttpMethod = method
    fun getPath(): String = path
    fun getBody(): String? = body
    fun getBaseUrl(): String = baseUrl
}
