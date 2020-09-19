package io.aklinker1.alarm.workers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.work.*
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.models.Alarm
import java.util.*

class AlarmScheduler(applicationContext: Context, workerParams: WorkerParameters) :
    Worker(applicationContext, workerParams) {

    companion object {
        private const val INPUT_ALARM_ID = "@alarm-schedule/id"

        fun updateAlarm(context: Context, alarm: Alarm) {
            Log.v("alarms", "AlarmScheduler.updateAlarm($alarm)")
            val request: WorkRequest = OneTimeWorkRequestBuilder<AlarmScheduler>()
                .setInputData(workDataOf(
                    INPUT_ALARM_ID to alarm.id
                ))
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    private val alarmManager =
        applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val alarmDao = AppDatabase.getInstance(applicationContext).alarmDao()

    override fun doWork(): Result {
        Log.v("alarms", "AlarmScheduler received work")
        val alarmId = inputData.getLong(INPUT_ALARM_ID, -1L)
        if (alarmId == -1L) {
            Log.w("alarms", "Attempted to schedule alarm without passing an id")
            return Result.failure()
        }

        try {
            val alarm = alarmDao.getSync(alarmId)
            cancelAlarm(alarm)
            if (alarm.enabled) {
                val pendingIntent = createPendingBroadcastIntent(alarm)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    val alarmInfo = AlarmManager.AlarmClockInfo(
                        Calendar.getInstance().timeInMillis + 5 * 1000,
                        pendingIntent
                    )
                    alarmManager?.setAlarmClock(alarmInfo, pendingIntent)
                    Log.v("alarms", "AlarmScheduler scheduled $alarm")
                }
            }
        } catch (err: Error) {
            Log.e("alarms", "Failed to setup alarm: $err")
            return Result.failure()
        }

        return Result.success()
    }

    private fun createPendingBroadcastIntent(alarm: Alarm): PendingIntent {
        return PendingIntent.getBroadcast(
            applicationContext,
            alarm.id.hashCode(),
            AlarmBroadcastReceiver.createIntent(applicationContext, alarm.id),
            0
        )
    }

    private fun cancelAlarm(alarm: Alarm) {
        alarmManager?.cancel(createPendingBroadcastIntent(alarm))
        Log.v("alarms", "AlarmScheduler canceled alarm for $alarm")
    }
}