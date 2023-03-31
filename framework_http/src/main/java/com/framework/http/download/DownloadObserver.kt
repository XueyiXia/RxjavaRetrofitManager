package com.framework.http.download

import android.annotation.SuppressLint
import android.os.Handler

import com.framework.http.download.model.DownloadBean
import com.framework.http.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable


import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.lang.ref.SoftReference

/**
 * 下载观察者(监听)
 * 备注:在此处监听: 开始下载 、下载错误 、下载完成  等状态
 *
 */
class DownloadObserver<T : DownloadBean?> constructor(

    private var downloadBean: DownloadBean,

    private val handler: Handler

) : IDownloadProgress, Observer<T> {

    private var disposable: Disposable? = null

    private var downloadCallback: SoftReference<DownloadCallback<*>?>

    private val isNeed: Boolean = false


    fun setDownload(downloadBean: DownloadBean) {
        this.downloadBean = downloadBean
        downloadCallback = SoftReference(downloadBean.getCallback())
    }

    /**
     * 开始下载/继续下载
     * 备注：继续下载需要获取之前下载的数据
     */
    @SuppressLint("CheckResult")
    public override fun onSubscribe(d: Disposable) {
        disposable = d
        downloadBean.setState(DownloadBean.State.WAITING) //等待状态

//        DBHelper.Companion.get()!!.insertOrUpdate(downloadBean) //更新数据库

        if (isNeed) {
            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            Observable.just(downloadBean.getCurrentSize()).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribe {
                    val progress: Float =
                        downloadBean.getCurrentSize().toFloat() / downloadBean.getTotalSize()
                            .toFloat()
                    /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                    //                            if(downloadBean.getState() != DownloadBean.State.PAUSE)return;
                    if (downloadCallback.get() != null) {
                        downloadCallback.get()!!.onProgress(
                            downloadBean.getState(),
                            downloadBean.getCurrentSize(),
                            downloadBean.getTotalSize(),
                            progress
                        )
                    }
                }
            return
        }
        if (downloadCallback.get() != null) { //回调
            val progress: Float = ComputeUtils.getProgress(downloadBean.getCurrentSize(), downloadBean.getTotalSize())
            downloadCallback.get()!!.onProgress(
                downloadBean.getState(),
                downloadBean.getCurrentSize(),
                downloadBean.getTotalSize(),
                progress
            )
        }
    }

    /**
     * 下载出错
     * 备注：回调进度，回调onError
     */
    public override fun onError(e: Throwable) {
        downloadBean.setState(DownloadBean.State.ERROR) //错误状态
        if (downloadCallback.get() != null) {
            val progress: Float =
                ComputeUtils.getProgress(downloadBean.getCurrentSize(), downloadBean.getTotalSize())
            downloadCallback.get()!!.onProgress(
                downloadBean.getState(),
                downloadBean.getCurrentSize(),
                downloadBean.getTotalSize(),
                progress
            )
            downloadCallback.get()!!.onError(e)
        }
    }

    /**
     * 下载完成
     * 备注：将开发者传入的Download子类回传
     */
    public override fun onNext(t: T) {
        downloadBean.setState(DownloadBean.State.FINISH) //下载完成
        if (downloadCallback.get() != null) { //回调
            downloadCallback.get()!!.onSuccess(t as Nothing)
        }
    }

    public override fun onComplete() {}

    /**
     * 进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    @SuppressLint("CheckResult")
    public override fun progress(currentSize: Long, totalSize: Long) {
        var currentSize: Long = currentSize
        if (downloadBean.getTotalSize() > totalSize) {
            currentSize += downloadBean.getTotalSize() - totalSize
        } else {
            downloadBean.setTotalSize(totalSize)
        }
        downloadBean.setCurrentSize(currentSize)
        if (isNeed) {
            /*接受进度消息，造成UI阻塞，如果不需要显示进度可去掉实现逻辑，减少压力*/
            Observable.just(currentSize).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribe(object : Consumer<Long?> {
                    @Throws(Exception::class)
                    public override fun accept(aLong: Long?) {
                        val progress: Float =
                            downloadBean.getCurrentSize().toFloat() / downloadBean.getTotalSize().toFloat()
                        /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                        if (downloadBean.getState() != DownloadBean.State.PAUSE) return
                        if (downloadCallback.get() != null) {
                            downloadCallback.get()!!.onProgress(
                                downloadBean.getState(),
                                downloadBean.getCurrentSize(),
                                downloadBean.getTotalSize(),
                                progress
                            )
                        }
                    }
                })
        } else {
            handler.post { /*下载进度==总进度修改为完成状态*/
                if ((downloadBean.getCurrentSize() == downloadBean.getTotalSize()) && (downloadBean.getTotalSize() != 0L)) {
                    downloadBean.setState(DownloadBean.State.FINISH)
                }
                /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                if (downloadBean.getState() != DownloadBean.State.PAUSE) {
                    val progress: Float =
                        downloadBean.getCurrentSize().toFloat() / downloadBean.getTotalSize()
                            .toFloat()

                    if (downloadCallback.get() != null) {
                        downloadCallback.get()!!.onProgress(
                            downloadBean.getState(),
                            downloadBean.getCurrentSize(),
                            downloadBean.getTotalSize(),
                            progress
                        )
                    }
                }
            }
        }
    }

    /**
     * 取消请求
     * 备注：暂停下载时调用
     */
    fun dispose() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    init {
        downloadCallback = SoftReference(
            downloadBean.getCallback()
        )
    }
}