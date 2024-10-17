package com.sai.mechat.notification

import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

object Token {
    fun getToken() : String {
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            return@addOnCompleteListener
        }
        return ""
    }
}