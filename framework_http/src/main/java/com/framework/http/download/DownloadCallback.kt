package com.framework.http.download

import com.framework.http.download.model.DownloadBean

/**
 * 下载回调
 *
 */
abstract class DownloadCallback<T : Any?> {
    /**
     * 进度回调
     *
     * @param state       下载状态
     * @param currentSize 当前已下载
     * @param totalSize   文件总大小
     * @param progress    进度
     */
    abstract fun onProgress(state: DownloadBean.State?, currentSize: Long, totalSize: Long, progress: Float)

    /**
     * 下载出错
     *
     * @param e
     */
    abstract fun onError(e: Throwable?)

    /**
     * 下载成功
     *
     * @param data
     */
    abstract fun onSuccess(data: T)
}