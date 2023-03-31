package com.framework.http.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * 下载ResponseBody
 *
 */
class DownloadResponseBody constructor(
    private val responseBody: ResponseBody?,
    private val downloadProgress: IDownloadProgress?
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    public override fun contentType(): MediaType? {
        return responseBody!!.contentType()
    }

    public override fun contentLength(): Long {
        return responseBody!!.contentLength()
    }

    public override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody!!.source()).buffer()
        }
        return (bufferedSource)!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var readBytesCount: Long = 0L
            var totalBytesCount: Long = 0L
            @Throws(IOException::class)
            public override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead: Long = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                readBytesCount += if (bytesRead != -1L) bytesRead else 0
                if (totalBytesCount == 0L) {
                    totalBytesCount = contentLength()
                }
//                Log.d("download progress readBytesCount:" + readBytesCount + "  totalBytesCount:" + totalBytesCount + " callback:" + downloadProgress)
                downloadProgress?.progress(readBytesCount, totalBytesCount)
                return bytesRead
            }
        }
    }
}