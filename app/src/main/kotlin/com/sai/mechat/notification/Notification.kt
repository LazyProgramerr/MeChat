package com.sai.mechat.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sai.mechat.activities.NotificationsActivity

object Notification {

    fun sendNotification(context: Context,bundle: Bundle){
        val builder = NotificationCompat.Builder(context, bundle.getString("channel",""))
            .setSmallIcon(android.R.drawable.star_off)
            .setContentTitle("title")
            .setContentText("message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(bundle.getInt("notificationId"),builder.build())
    }


}