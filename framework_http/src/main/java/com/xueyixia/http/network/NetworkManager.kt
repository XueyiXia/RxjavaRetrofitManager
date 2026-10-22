package com.xueyixia.http.network

import com.xueyixia.http.callback.ApiCallback
import com.xueyixia.http.config.NetworkConfig
import com.xueyixia.http.exception.ApiException
import com.xueyixia.http.utils.HttpConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 这层负责真正发起请求，并将底层 Retrofit 结果转成统一异常/回调。
 * 这样业务层只需要知道 ApiCallback，不需要关心 Retrofit 的复杂实现细节。
 */
object NetworkManager {

    fun request(
        path: String,
        method: com.xueyixia.http.core.HttpMethod = com.xueyixia.http.core.HttpMethod.GET,
        params: Map<String, Any> = emptyMap(),
        headers: Map<String, String> = NetworkConfig.getHeaders(),
        callback: ApiCallback<String>
    ) {
        val service = RetrofitFactory.create(NetworkConfig.getBaseUrl())
            .create(ApiService::class.java)

        val call = when (method) {
            com.xueyixia.http.core.HttpMethod.GET -> service.get(path, params, headers)
            com.xueyixia.http.core.HttpMethod.POST -> service.post(path, params, headers)
            com.xueyixia.http.core.HttpMethod.PUT -> service.put(path, params, headers)
            com.xueyixia.http.core.HttpMethod.DELETE -> service.delete(path, params, headers)
            com.xueyixia.http.core.HttpMethod.HEAD -> service.head(path)
        }

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.onSuccess(body)
                    } else {
                        callback.onError(ApiException(response.code(), "empty response body"))
                    }
                } else {
                    callback.onError(
                        ApiException(
                            response.code(),
                            "HTTP ${response.code()}: ${response.message()}"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onError(ApiException(-1, t.message ?: "network error", t))
            }
        })
    }

    fun builder(baseUrl: String = NetworkConfig.getBaseUrl()): HttpRequestBuilder {
        return HttpRequestBuilder(baseUrl)
    }
}
