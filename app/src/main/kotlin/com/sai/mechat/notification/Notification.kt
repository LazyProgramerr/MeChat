package com.sai.mechat.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sai.mechat.activities.NotificationsActivity
import com.sai.mechat.constants.NotificationConstants.MESSAGE_CHANNEL_ID

object Notification {

    fun sendNotification(context: Context,bundle: Bundle){
        notificationChannels.createNotificationChannels(context)
        val builder = NotificationCompat.Builder(context, bundle.getString("channelId",MESSAGE_CHANNEL_ID))
            .setSmallIcon(android.R.drawable.star_off)
            .setContentTitle(bundle.getString("title",""))
            .setContentText(bundle.getString("message",""))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(bundle.getInt("notificationId"),builder.build())
    }


}