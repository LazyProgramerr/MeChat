package com.sai.mechat.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST

class FriendRequestResponseWorker(context: Context,workerParameters: WorkerParameters) : Worker(context,workerParameters) {
    override fun doWork(): Result {

        val response = inputData.getBoolean(
            "response",
            defaultValue = true
        )
        val fuser = FirebaseAuth.getInstance().currentUser
        if (response && fuser != null){
            setDataInFriends(fuser,inputData)
            return Result.success()
        }
        return Result.failure()
    }

    private fun setDataInFriends( fUser: FirebaseUser, inputData: Data) {
        val notificationsRef = FirebaseDatabase.getInstance().getReference("users")
        inputData.getString("senderId")
            ?.let { notificationsRef.child(fUser.uid).child(TYPE_FRIEND_REQUEST).child("sent").child(it).removeValue() ; notificationsRef.child(fUser.uid).child("friends").child(it).setValue(true)}
    }
}