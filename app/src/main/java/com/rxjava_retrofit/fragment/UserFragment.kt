package com.rxjava_retrofit.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.framework.http.bean.NotificationInfo
import com.framework.http.service.DownloadService
import com.framework.http.utils.NotificationHelper
import com.rxjava_retrofit.HttpApi
import com.rxjava_retrofit.R
import com.rxjava_retrofit.activities.MainActivity


/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class UserFragment :Fragment(){

    private var mRootView: View? = null

    private lateinit var mBtnDownload: Button

    private lateinit var mBtnNotify: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_user, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBtnDownload=view.findViewById(R.id.download)
        mBtnNotify=view.findViewById(R.id.notify)

        mBtnDownload.setOnClickListener {
            val intent= Intent(context, DownloadService::class.java)
            intent.putExtra("url", HttpApi.URL_DOWNLOAD)
            intent.putExtra("md5","BBFDF5D996224C643402E7B1162ADC27")
            context?.startService(intent)
        }

        mBtnNotify.setOnClickListener {

            context?.let {
                showNotification()


//               var notificationManager =it. getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                val notificationInfo= NotificationInfo("001","Category","001","Download")
//                val pendingIntent= PendingIntent.getActivity(it, 0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
//
//                var notificationBuilder = NotificationHelper.getNotificationBuilder(it,notificationInfo )
//                    .setOnlyAlertOnce(true)
//                    .setSmallIcon(com.framework.http.R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent)
//                    .setContentTitle("正在下载新版本,请稍等...")
//                    .setAutoCancel(true)
//                    .setOngoing(true)
//                    .setProgress(100, 0, false);
//
//                notificationManager.notify(20, notificationBuilder.build())
            }

        }

    }


    private fun showNotification() {
        context?.let {
            val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification: Notification

            //pendingIntent生成规则
            val notifyIntent = Intent()
            notifyIntent.setClass(it, MainActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(it, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("0", "notify", NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)

                val builder: Notification.Builder = Notification.Builder(it, "0")
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Build.VERSION_CODES.O")
                    .setOnlyAlertOnce(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setProgress(100, 0, false);
                builder.build()

            } else {

                val builder = NotificationCompat.Builder(it,"0")
                builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("test")
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .setProgress(100, 0, false);
                builder.build()
            }
            manager.notify(1000, notification)
        }

    }

}