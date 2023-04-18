package com.framework.http.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.framework.http.bean.NotificationInfo

/**
 * @author: xiaxueyi
 * @date: 2023-03-31
 * @time: 15:27
 * @说明:
 */
object NotificationHelper {

    //获取 Notification Builder
    @SuppressLint("WrongConstant")
    fun getNotificationBuilder(context: Context, notificationInfo: NotificationInfo): NotificationCompat.Builder {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val group = NotificationChannelGroup(
                notificationInfo.getGroupId(),
                notificationInfo.getGroupName()
            )
            notificationManager.createNotificationChannelGroup(group)
            val channel = NotificationChannel(
                notificationInfo.getChannelId(),
                notificationInfo.getChannelName(),
                notificationInfo.getImportance()
            )
            channel.group = notificationInfo.getGroupId()
            notificationManager.createNotificationChannel(channel)
            notificationInfo.getChannelId().let { builder.setChannelId(it) }
        }
        return builder
    }

    //判断通知栏功能是否开启
    fun isNotficationEnabled(context: Context): Boolean {
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        return notificationManager.areNotificationsEnabled()
    }

    //判断渠道通知是否开启
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun isChannelsEnabled(context: Context, channelId: String?): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = notificationManager.getNotificationChannel(channelId)
        return !(notificationChannel != null && notificationChannel.importance == NotificationManager.IMPORTANCE_NONE)
    }

    //跳转到渠道设置
    fun gotoChannelSetting(context: Context, channelId: String?) {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        context.startActivity(intent)
    }

    //跳转到通知设置界面
    fun gotoNotificationSetting(context: Context) {
        try {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            } else {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.parse("package:" + context.packageName)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            IntentHelper.gotoAppSettings(context)
        }
    }
}