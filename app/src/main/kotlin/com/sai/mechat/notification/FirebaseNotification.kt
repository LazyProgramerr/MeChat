package com.sai.mechat.notification



import android.os.Bundle
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sai.mechat.workers.NotificationWorker

class FirebaseNotification : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM result", "onMessageReceived() returned: "+message.data.toString())

        // Prepare Data object to pass the complete payload to NotificationWorker
        val workRequestData = Data.Builder().apply {
            // Add notification payload, if available
//            message.notification?.let {
//                putString("notification_title", it.title)
//                putString("notification_body", it.body)
//                putString("notification_imageUrl", it.imageUrl?.toString())
//            }

            // Add data payload
            message.data.forEach { entry ->
                putString(entry.key, entry.value)
            }
        }.build()

        // Trigger the worker
        val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(workRequestData)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)

//        val bundle = Bundle()


//        bundle.putString("title", message.data["title"])
//        bundle.putString("message", message.data["message"])
//        bundle.putString("image", message.data["images"])
//        bundle.putString("channel", message.data["channelId"])


//        Notification.sendNotification(this,bundle)

    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            val tokenRef = FirebaseDatabase.getInstance().getReference("users").child(user.uid)
            tokenRef.child("Token").setValue(token)
        }
    }


}