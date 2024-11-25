package com.sai.mechat.data

import org.json.JSONObject

data class NotificationData(
    val title:String,
    val body:String,
    val token:String,
    val data:JSONObject
)
