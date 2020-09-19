package io.aklinker1.alarm.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.aklinker1.alarm.R
import io.aklinker1.alarm.activities.ActiveAlarmActivity
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.NotificationChannels

class AlarmBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val EXTRA_ALARM = "io.aklinker1.alarm.workers.AlarmBroadcastReceiver#EXTRA_ALARM"
        private const val INTENT_ACTION = "io.aklinker1.alarm.workers.AlarmBroadcastReceiver#ALARM_BROADCAST"

        fun createIntent(context: Context, alarmId: Long): Intent {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            intent.action =  INTENT_ACTION
            intent.putExtra(EXTRA_ALARM, alarmId)
            return intent
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationChannels.init(context!!)
        val alarmId = intent?.getLongExtra(EXTRA_ALARM, -1) ?: -1

        val pendingIntent = ActiveAlarmActivity.navigate(context, alarmId)
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
