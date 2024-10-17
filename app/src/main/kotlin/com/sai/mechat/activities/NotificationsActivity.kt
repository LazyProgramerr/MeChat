package com.sai.mechat.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sai.mechat.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {
    private lateinit var views: ActivityNotificationsBinding
    private var showNotification:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(views.root)



    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotificationFromTIRAMISU(channelId : String, title : String, message : String, notificationId: Int){
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.star_off)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(this@NotificationsActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

            }else
                notify(notificationId,builder.build())
        }
    }
    fun showNotificationFromOreo(channelId : String, title : String, message : String, notificationId : Int){
        val builder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(android.R.drawable.star_off)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        notificationManager.notify(notificationId,builder.build())

    }


    val requestNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted:Boolean ->
        showNotification = isGranted
    }
}