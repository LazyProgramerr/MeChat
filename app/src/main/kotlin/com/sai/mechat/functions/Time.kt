package com.sai.mechat.functions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Time {
    public fun readableTime(timeStamp: Long): String {
        val date = Date(timeStamp)
        val format = SimpleDateFormat("hh:mm:ss aa", Locale.getDefault())
        return format.format(date)
    }
}
