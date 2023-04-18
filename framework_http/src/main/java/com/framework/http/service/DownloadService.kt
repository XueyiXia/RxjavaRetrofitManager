package com.framework.http.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.framework.http.R
import com.framework.http.bean.DownloadInfo
import com.framework.http.bean.NotificationInfo
import com.framework.http.callback.DownloadCallback
import com.framework.http.config.DownloadConfigure
import com.framework.http.http.RxHttp
import com.framework.http.utils.NotificationHelper
import com.framework.http.utils.StorageHelper
import com.framework.http.utils.StringUtils
import java.io.File
import java.text.DecimalFormat
import java.util.*


/**
 * @author: xiaxueyi
 * @date: 2023-03-30
 * @time: 15:40
 * @说明: 下载APK
 */
class DownloadService : Service(), LifecycleEventObserver {
    companion object {
        const val TAG = "DownloadService"
        const val DOWNLOAD_NOTIFY_ID = 1005
        const val MSG_SHOW_NOTIFICATION = 0
        const val MSG_UPDATE_NOTIFICATION = 1
        const val MSG_CANCEL_NOTIFICATION = 2
        const val MSG_INSTALL_APK = 3
    }

    lateinit var dir: String
    lateinit var filename: String

    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager
    private var handlerThread: HandlerThread? = null
    private var handler: DownloadHandler? = null

    private var urlList= mutableListOf<String>()




    /**
     * 处理更新通知栏的操作放在子线程操作，避免影响主线程
     */
    inner class DownloadHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SHOW_NOTIFICATION -> {
                    notificationManager.notify(DOWNLOAD_NOTIFY_ID, notificationBuilder.build())
                }
                MSG_UPDATE_NOTIFICATION -> {
                    val pair = msg.obj as Pair<Long, Long>
                    val progress = pair.first
                    val total = pair.second
                    val percentFloat = DecimalFormat("0.00").format(progress * 1.0f / total)
                    val percentInt = (percentFloat.toDouble() * 100).toInt()
                    Log.d(TAG, "handleMessage() called with: progress = ${progress}, total = ${total},percentFloat:${percentFloat},percentInt:${percentInt}")
                    var content = ""
                    content = if (percentInt < 100) {
                        applicationContext.resources.getString(R.string.download_progress, percentInt)
                    } else {
                        applicationContext.resources.getString(R.string.download_success)
                    }
                    notificationBuilder.setContentText(content).setProgress(100, percentInt, false)
                    notificationManager.notify(DOWNLOAD_NOTIFY_ID, notificationBuilder.build())
                }
                MSG_CANCEL_NOTIFICATION -> {
                    notificationManager.cancel(DOWNLOAD_NOTIFY_ID)
                }
                MSG_INSTALL_APK -> {
                    StringUtils.installApk(applicationContext, File(dir, filename), "$packageName.fileprovider")
                }
            }
        }
    }


    private var downloadCallback = object : DownloadCallback<DownloadInfo>() {

        var url:String=""

        override fun onStart() {
            handler?.sendEmptyMessage(MSG_SHOW_NOTIFICATION)
        }


        override fun onProgress(readBytes: Long, totalBytes: Long) {
            Log.d(TAG, "onProgress() called with: readBytes = $readBytes, totalBytes = $totalBytes")
            handler?.sendMessage(Message.obtain(handler, MSG_UPDATE_NOTIFICATION, Pair(readBytes, totalBytes)))

        }

        override fun onNext(response: DownloadInfo?) {
            Log.d(TAG, "onNext() called with: response = $response")
            handler?.sendEmptyMessage(MSG_INSTALL_APK)
        }

        override fun onError(e: Throwable?) {
            Log.d(TAG, "onError() called with: e = $e")
            handler?.sendEmptyMessage(MSG_CANCEL_NOTIFICATION)
            urlList.remove(url)
            Log.d(TAG, "onError() called,urlList-size:${urlList.size}")
        }

        override fun onComplete() {
            Log.d(TAG, "onComplete() called")
            handler?.sendEmptyMessage(MSG_CANCEL_NOTIFICATION)
            urlList.remove(url)
            Log.d(TAG, "onComplete() called,urlList-size:${urlList.size}")
        }

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate() {
        super.onCreate()

        dir = StorageHelper.getExternalSandBoxPath(applicationContext, Environment.DIRECTORY_DOWNLOADS)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationHelper.getNotificationBuilder(
                applicationContext,
                NotificationInfo("001", "Category", "001", "Download")
        )
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(
                        PendingIntent.getActivity(
                                applicationContext,
                                0,
                                Intent(),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                )
                .setContentTitle("正在下载新版本,请稍等...")
                .setAutoCancel(true)
                .setOngoing(true)
                .setProgress(100, 0, false);


        val handlerThread = HandlerThread("DownloadHandlerThread")
        handlerThread.start()
        handler = DownloadHandler(handlerThread.looper)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val urlLink = intent?.getStringExtra("url")
        val md5=intent?.getStringExtra("md5")
        if(!TextUtils.isEmpty(urlLink)){
            if(urlList.contains(urlLink)){
//                ToastHelper.toast("url:${urlLink} is exists in downloading!!!")
//                return super.onStartCommand(intent, flags, startId)
            }
            urlList.add(urlLink!!)
            Log.d(TAG, "onStartCommand called,urlList-size:${urlList.size}")
            filename = urlLink.substring(urlLink.lastIndexOf(File.separator) + 1)
            if (TextUtils.isEmpty(filename)) {
                filename = UUID.randomUUID().toString()
            }
            val downloadConfigure: DownloadConfigure = DownloadConfigure.get()
            downloadConfigure.setDirectoryFile(StorageHelper.getExternalSandBoxPath(this, Environment.DIRECTORY_DOWNLOADS))
            downloadConfigure.setFileName("down")
            downloadConfigure.setDownloadUrl(urlLink)
            downloadConfigure.setMd5(md5)
            RxHttp.getRxHttpBuilder()
                .setDownloadConfigure(downloadConfigure)
                .setLifecycle(null)
                .build()
                .execute(downloadCallback)
//            NetworkRepository.instance.httpDownload(
//                    context = applicationContext,
//                    url = urlLink,
//                    dir = dir,
//                    filename = filename,
//                    callback = downloadCallback.apply {
//                        url=urlLink
//                    },
//                    md5 =md5
//            )
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handlerThread?.quit()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }


}