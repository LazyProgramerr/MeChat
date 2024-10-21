package com.sai.mechat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.sai.mechat.constants.NotificationConstants.ALERT_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.EVENT_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.MESSAGE_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.PROMO_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.REMAINDER_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.UPDATE_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.UPLOAD_DOWNLOAD_CHANNEL_ID

object notificationChannels {

    fun createNotificationChannels(context: Context) {

        val messageChannel = NotificationChannel(MESSAGE_CHANNEL_ID,"messages",NotificationManager.IMPORTANCE_HIGH).apply { description = "for user messages" }

        val uploadDownload = NotificationChannel(UPLOAD_DOWNLOAD_CHANNEL_ID,"upload/download",NotificationManager.IMPORTANCE_LOW).apply { description = "for upload and download status" }

        val updateChannel = NotificationChannel(UPDATE_CHANNEL_ID,"update",NotificationManager.IMPORTANCE_LOW).apply { description = "for app updates" }

        val promo = NotificationChannel(PROMO_CHANNEL_ID,"Promotional Notifications",NotificationManager.IMPORTANCE_LOW).apply { description = "for promo\'s" }

        val alert = NotificationChannel(ALERT_CHANNEL_ID,"alert\'s",NotificationManager.IMPORTANCE_LOW).apply { description = "for alert\'s" }

        val remainder = NotificationChannel(REMAINDER_CHANNEL_ID,"remainder\'s",NotificationManager.IMPORTANCE_DEFAULT).apply { description = "for remainder\'s" }

        val event = NotificationChannel(EVENT_CHANNEL_ID,"event\'s",NotificationManager.IMPORTANCE_LOW).apply { description = "for event\'s" }


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(messageChannel)
        manager.createNotificationChannel(updateChannel)
        manager.createNotificationChannel(uploadDownload)
        manager.createNotificationChannel(promo)
        manager.createNotificationChannel(alert)
        manager.createNotificationChannel(remainder)
        manager.createNotificationChannel(event)
    }
}