package com.sai.mechat.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST

class FriendRequestWorker(context: Context,workerParameters: WorkerParameters): Worker(context,workerParameters) {
    override fun doWork(): Result {

        val senderId = inputData.getString("senderId")
        val message = inputData.getString("message")
        val time = System.currentTimeMillis().toString()

        val uData = Data.Builder()
            .putAll(inputData)
            .putString("id",time)
            .build()

        val firebaseUser = FirebaseAuth.getInstance().currentUser


        if (firebaseUser != null) {
            setDataInNotificationsList(time,firebaseUser,uData)
            Log.d("FriendRequestWorker","data : $uData")
            return Result.success()

        }

        return Result.failure()
    }

    private fun setDataInNotificationsList(time:String,fUser: FirebaseUser, inputData: Data) {
        val notificationsRef = FirebaseDatabase.getInstance().getReference("users")
        notificationsRef.child(fUser.uid).child("Notifications").child(time).setValue(inputData.keyValueMap)
        inputData.getString("senderId")
            ?.let { notificationsRef.child(fUser.uid).child(TYPE_FRIEND_REQUEST).child("received").child(it).setValue(false) }
    }
}