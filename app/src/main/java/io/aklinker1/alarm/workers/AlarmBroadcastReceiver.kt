package io.aklinker1.alarm.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.aklinker1.alarm.activities.AlarmActivity

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("alarms", "AlarmBroadcastReceiver.onReceive")
        val activityIntent = Intent(context, AlarmActivity::class.java)
        activityIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activityIntent.putExtra("alarm", intent?.getLongExtra("alarm", -1L))
        context!!.startActivity(activityIntent)
    }

}
