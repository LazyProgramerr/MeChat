package com.sai.mechat.data

data class FriendRequestResponse(
    val token:String,
    val senderId: String,
    val message: String,
    val response:Boolean,
    val notificationType: String,
)
