package com.sai.mechat.notification

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

object Token {
    fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                return@addOnCompleteListener
            }
        }
    }
}