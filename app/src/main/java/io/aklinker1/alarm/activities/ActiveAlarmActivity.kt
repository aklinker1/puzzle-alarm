package io.aklinker1.alarm.activities

import android.app.PendingIntent
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import io.aklinker1.alarm.ActiveAlarmActivityNavGraphArgs
import io.aklinker1.alarm.R
import io.aklinker1.alarm.utils.RingtoneUtils
import io.aklinker1.alarm.utils.turnScreenOffAndKeyguardOn
import io.aklinker1.alarm.utils.turnScreenOnAndKeyguardOff
import io.aklinker1.alarm.workers.AlarmScheduler
import java.util.*


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

    var sound: Ringtone? = null
    var isSoundPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_alarm)
        turnScreenOnAndKeyguardOff()
        val ringtone = RingtoneUtils.getDefaultRingtone()

        loopRingtone(ringtone)
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
        sound?.stop()
        AlarmScheduler.updateSchedule(this)
    }

    fun loopRingtone(uri: Uri) {
        sound = RingtoneManager.getRingtone(applicationContext, uri).apply {
            play()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                isLooping = true
            }
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (sound?.isPlaying != true && !isSoundPaused) {
                        sound?.play()
                    }
                }
            }, 1000 * 1, 1000 * 1)
        }

    }
}
