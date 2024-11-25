package com.sai.mechat.functions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Time {
    public fun readableTime(timeStamp: Long,pattern: String): String {
        val date = Date(timeStamp)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }
}
