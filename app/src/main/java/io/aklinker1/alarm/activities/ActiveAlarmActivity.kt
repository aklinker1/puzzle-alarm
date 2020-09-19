package io.aklinker1.alarm.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDeepLinkBuilder
import io.aklinker1.alarm.ActiveAlarmActivityNavGraphArgs
import io.aklinker1.alarm.R
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.turnScreenOffAndKeyguardOn
import io.aklinker1.alarm.utils.turnScreenOnAndKeyguardOff

class ActiveAlarmActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context, alarmId: Long): PendingIntent {
            val args = ActiveAlarmActivityNavGraphArgs(alarmId)
            return NavDeepLinkBuilder(context)
                .setComponentName(ActiveAlarmActivity::class.java)
                .setGraph(R.navigation.active_alarm_activity_nav_graph)
                .setDestination(R.id.active_alarm_fragment)
                .setArguments(args.toBundle())
                .createPendingIntent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_alarm)
        turnScreenOnAndKeyguardOff()
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}
