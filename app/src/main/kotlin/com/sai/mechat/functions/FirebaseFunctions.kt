package com.sai.mechat.functions

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseFunctions(context: Context) {
    fun GetFirebaseToken(userId : String):String{
        var token = ""

        val db = FirebaseDatabase.getInstance().getReference("users")
        db.child(userId).child("Token").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    token = snapshot.value.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        return token
    }
}