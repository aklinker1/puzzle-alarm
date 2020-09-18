package io.aklinker1.alarm.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.aklinker1.alarm.R

object NotificationChannels {

    const val CHANNEL_ALARM = "alarm"

    fun init(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Alarm Fullscreen Channel
        manager.apply {
            val name = context.getString(R.string.notification_alarm_channel_name)
            val description = context.getString(R.string.notification_alarm_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(CHANNEL_ALARM, name, importance).apply {
                    setDescription(description)
                }
            createNotificationChannel(channel)
        }
    }
}