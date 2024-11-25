package com.sai.mechat.data

data class FriendRequest(
    val token:String,
    val senderId: String,
    val message: String,
    val notificationType: String,
)
