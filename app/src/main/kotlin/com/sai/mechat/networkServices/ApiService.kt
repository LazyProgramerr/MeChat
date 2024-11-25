package com.sai.mechat.networkServices

import com.sai.mechat.data.DataRequest
import com.sai.mechat.data.FriendRequest
import com.sai.mechat.data.FriendRequestResponse
import com.sai.mechat.data.NotificationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("sendNotification") // Endpoint URL after the base URL
    fun registerNotification(@Body notificationData: NotificationData): Call<Void>

    @POST("sendData")
    fun registerData(@Body dataRequest: DataRequest):Call<Void>

    @POST("friend_requests")
    fun registerFriendRequest(@Body friendRequest: FriendRequest): Call<Void>

    @POST("friend_requests")
    fun responseFriendRequest(@Body response: FriendRequestResponse): Call<Void>

}