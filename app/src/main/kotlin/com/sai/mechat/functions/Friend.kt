package com.sai.mechat.functions

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Friend {

    fun AddFriend(context: Context,senderId: String,receiverId:String){
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("users")
        firebaseDatabase.child(receiverId).child("Token").get().addOnSuccessListener { task ->
            val token = task.value.toString()


        }

    }

}