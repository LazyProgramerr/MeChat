package com.sai.mechat.functions

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object functions {

    fun vibrate(context: Context,duration: Long){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 (API level 31) and above, use VibratorManager
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // For older versions, use the traditional Vibrator
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}