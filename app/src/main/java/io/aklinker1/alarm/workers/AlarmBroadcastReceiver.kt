package io.aklinker1.alarm.workers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.aklinker1.alarm.R
import io.aklinker1.alarm.activities.AlarmActivity
import io.aklinker1.alarm.utils.NotificationChannels

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationChannels.init(context!!)

        val activityIntent = Intent(context, AlarmActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context, NotificationChannels.CHANNEL_ALARM)
            .setSmallIcon(R.drawable.ic_add)
            .setContentTitle("Alarm")
            .setContentText("is going off")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setFullScreenIntent(pendingIntent, true)
        val notification = notificationBuilder.build()
        NotificationManagerCompat.from(context).notify(0, notification)
    }

}
