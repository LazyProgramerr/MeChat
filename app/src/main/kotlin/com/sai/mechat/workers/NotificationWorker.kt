package com.sai.mechat.workers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sai.mechat.R
import com.sai.mechat.activities.MainActivity
import com.bumptech.glide.Glide
import com.sai.mechat.constants.NotificationConstants.MESSAGE_CHANNEL_ID
import com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST
import com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST_RESPONSE
import com.sai.mechat.constants.NotificationConstants.TYPE_NOTIFICATION

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve notification data
        val title = inputData.getString("title") ?: "Default Title"
        val body = inputData.getString("notification_body") ?: "Default Body"
        val imageUrl = inputData.getString("notification_imageUrl")

        val notificationType = inputData.getString("notificationType") ?: TYPE_NOTIFICATION


        if (notificationType == TYPE_FRIEND_REQUEST){
            val workFriendRequest = OneTimeWorkRequest.Builder(FriendRequestWorker::class.java)
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(workFriendRequest)
            inputData.getString("message")?.let { showNotification("Friend Request", message = it,null) }
            return Result.success()
        }else if (notificationType == TYPE_NOTIFICATION){
            showNotification(title, body, imageUrl)
            return Result.success()
        }else if(notificationType == TYPE_FRIEND_REQUEST_RESPONSE){
            val workFriendRequestResponse = OneTimeWorkRequest.Builder(FriendRequestResponseWorker::class.java)
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(workFriendRequestResponse)
            inputData.getString("message")?.let { showNotification("friend Request Response", it,null) }
            return Result.success()
        }else {
            return Result.failure()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showNotification(title: String, message: String, imageUrl: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Ensure notification channel creation only for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = MESSAGE_CHANNEL_ID
            val channel = NotificationChannel(
                channelId,
                "FCM Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to open MainActivity
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "fcm_default_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.flash)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Load image if available
        imageUrl?.let {
            val bitmap = Glide.with(applicationContext)
                .asBitmap()
                .load(it)
                .submit()
                .get()
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
