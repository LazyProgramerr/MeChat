package com.sai.mechat.notification

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotification : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val bundle = Bundle()

        bundle.putString("title",message.data.get("title"))
        bundle.putString("message",message.data.get("message"))
        bundle.putString("image",message.data.get("images"))
        bundle.putString("channel",message.data.get("channel"))


        Notification.sendNotification(this,bundle)

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