package com.framework.http.requestbody

import com.framework.http.callback.ProgressCallback
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(private val responseBody: ResponseBody, private val callback: ProgressCallback?) : ResponseBody() {

    private var progressSource: BufferedSource? = null
    private var contentLength = 0L
    private var lastProgressBytes = 0L
    private val minProgressStepBytes = 32L * 1024

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        if (contentLength == 0L) {
            contentLength = responseBody.contentLength()
        }
        return contentLength
    }

    override fun source(): BufferedSource {
        if (callback == null) {
            return responseBody.source()
        }
        if (progressSource == null) {
            progressSource = source(responseBody.source()).buffer()
        }
        return progressSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead == -1L) {
                    callback?.onProgress(contentLength(), contentLength())
                    return bytesRead
                }

                totalBytesRead += bytesRead
                val shouldReport = totalBytesRead == contentLength() ||
                    (totalBytesRead - lastProgressBytes) >= minProgressStepBytes ||
                    (contentLength() > 0 && totalBytesRead * 100 / contentLength() != (lastProgressBytes * 100 / contentLength()))
                if (shouldReport) {
                    lastProgressBytes = totalBytesRead
                    callback?.onProgress(totalBytesRead, contentLength())
                }
                return bytesRead
            }
        }
    }

    override fun close() {
        try {
            progressSource?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
