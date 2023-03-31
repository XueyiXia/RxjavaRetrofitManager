package com.framework.http.download

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 通过Interceptor回调监听Response进度
 *
 */
class DownloadInterceptor constructor(private val downloadProgress: IDownloadProgress) : Interceptor {
    @Throws(IOException::class)
    public override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val downloadResponseBody=DownloadResponseBody(response.body, downloadProgress)
        return response.newBuilder()
            .body(downloadResponseBody)
            .build()
    }
}