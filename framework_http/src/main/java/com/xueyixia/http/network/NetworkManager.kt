package com.xueyixia.http.network

import com.xueyixia.http.callback.ApiCallback
import com.xueyixia.http.config.NetworkConfig
import com.xueyixia.http.core.HttpMethod
import com.xueyixia.http.exception.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Main request dispatcher.
 */
object NetworkManager {

    fun request(
        path: String,
        method: HttpMethod = HttpMethod.GET,
        params: Map<String, Any> = emptyMap(),
        headers: Map<String, String> = NetworkConfig.getHeaders(),
        callback: ApiCallback<String>
    ) {
        val service = RetrofitFactory.create(NetworkConfig.getBaseUrl())
            .create(ApiService::class.java)

        val call = when (method) {
            HttpMethod.GET -> service.get(path, params, headers)
            HttpMethod.POST -> service.post(path, params, headers)
            HttpMethod.PUT -> service.put(path, params, headers)
            HttpMethod.DELETE -> service.delete(path, params, headers)
            HttpMethod.HEAD -> service.head(path)
        }

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.onSuccess(body)
                    } else {
                        callback.onError(ApiException(response.code(), "Empty response body"))
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
