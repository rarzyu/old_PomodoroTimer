package com.example.pomodorotimer.Common

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi

open class NotificationController(private val context: Context?) {

    @RequiresApi(Build.VERSION_CODES.O)
    open fun vibrate() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    open fun playNotificationSound() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, notification)
        r.play()
    }
}

//プレビューのダミー用
class DummyNotificationController : NotificationController(null) {
    override fun vibrate() {
        // 何もしない
    }

    override fun playNotificationSound() {
        // 何もしない
    }
}