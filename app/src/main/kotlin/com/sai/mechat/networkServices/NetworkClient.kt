package com.sai.mechat.networkServices

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST
import com.sai.mechat.data.DataRequest
import com.sai.mechat.data.FriendRequest
import com.sai.mechat.data.FriendRequestResponse
import com.sai.mechat.data.NotificationData
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkClient {

    private val apiService: ApiService

    init {
        // Initialize Retrofit with your backend URL
        val retrofit = Retrofit.Builder()
            .baseUrl("https://mechat.pythonanywhere.com/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun sendNotification(
        to: String,
        title: String,
        body: String,
        data: JSONObject
    ) {
        val notificationRequest = NotificationData(title = title, body = body, token = to, data = data)

        apiService.registerNotification(notificationRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("NetworkClient", if (response.isSuccessful) "Notification sent successfully!" else "Failed to send notification: ${response.errorBody()?.string()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NetworkClient", "Request failed: ${t.message}")
            }
        })
    }

    fun sendData(
        to: String,
        data: JSONObject
    ){
        val dataRequest = DataRequest(token = to, data = data)
        apiService.registerData(dataRequest).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("NetworkClient",if (response.isSuccessful) "Data sent successfully" else "Failed to send Data: ${response.errorBody()?.string()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NetworkClient", "Request failed: ${t.message}")
            }

        })
    }
    fun sendFriendRequest(
        token: String,
        senderId: String,
        message: String,
        notificationType: String,
        toId: String,
    ){
        val friendRequest = FriendRequest(token = token, message = message, senderId = senderId, notificationType = notificationType)
        apiService.registerFriendRequest(friendRequest).enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("NetworkClient",if (response.isSuccessful) "Friend Request sent successfully :"+response.raw() else "Failed to send Friend Request: ${response.errorBody()?.string()}")
                saveRequestInfo(toId,senderId)
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NetworkClient", "Request failed: ${t.message}")
            }
        })
    }

    fun sendFriendRequestResponse(
        token: String,
        senderId: String,
        message: String,
        response: Boolean,
        notificationType: String,

    ){
        val responseFriendRequestResponse = FriendRequestResponse(
            token = token,
            message = message,
            senderId = senderId,
            response = response,
            notificationType = notificationType,
        )
        apiService.responseFriendRequest(responseFriendRequestResponse).enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("NetworkClient",if (response.isSuccessful) "Friend Request Response sent successfully :"+response.raw() else "Failed to send Friend Request Response: ${response.errorBody()?.string()}")

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NetworkClient", "Request failed: ${t.message}")
            }

        })
    }

    private fun saveRequestInfo(to: String,from: String) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(from)
        database.child(TYPE_FRIEND_REQUEST).child("sent").child(to).setValue(false)

    }
}
