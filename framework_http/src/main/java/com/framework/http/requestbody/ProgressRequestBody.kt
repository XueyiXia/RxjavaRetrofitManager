package com.framework.http.requestbody

import com.framework.http.callback.ProgressCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

class ProgressRequestBody(private val requestBody: RequestBody, private val callback: ProgressCallback?) : RequestBody() {

    private var progressSink: BufferedSink? = null
    private var contentLength = 0L
    private var lastProgressBytes = 0L
    private val minProgressStepBytes = 32L * 1024

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        if (contentLength == 0L) {
            contentLength = requestBody.contentLength()
        }
        return contentLength
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (callback == null) {
            requestBody.writeTo(sink)
            return
        }
        if (progressSink == null) {
            progressSink = sink(sink).buffer()
        }
        callback.onProgress(0, contentLength())
        requestBody.writeTo(progressSink!!)
        progressSink?.apply {
            flush()
            close()
        }
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            var totalBytesWrite = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                totalBytesWrite += byteCount
                val shouldReport = totalBytesWrite == contentLength() || (totalBytesWrite - lastProgressBytes) >= minProgressStepBytes
                if (shouldReport) {
                    lastProgressBytes = totalBytesWrite
                    callback?.onProgress(totalBytesWrite, contentLength())
                }
            }
        }
    }
}
