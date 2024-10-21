package com.sai.mechat.functions

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View

object Animation {
    fun vibrate (context: Context,view : View,duration: Long){
        val animator = ObjectAnimator.ofFloat(view,"translationX",0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        animator.duration = duration
        functions.vibrate(context,duration)
        animator.start()

    }
}