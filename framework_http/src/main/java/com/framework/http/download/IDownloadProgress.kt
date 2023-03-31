package com.framework.http.download

/**
 * 下载进度回调接口
 *
 */
open interface IDownloadProgress {
    /**
     * 下载进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    fun progress(currentSize: Long, totalSize: Long)
}