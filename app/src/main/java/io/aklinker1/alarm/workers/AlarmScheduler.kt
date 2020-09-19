package io.aklinker1.alarm.workers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.models.Alarm
import java.util.*

class AlarmScheduler(applicationContext: Context, workerParams: WorkerParameters) :
    Worker(applicationContext, workerParams) {

    companion object {
        private const val REQUEST_CODE_NEXT_ALARM = 14829

        fun updateSchedule(context: Context) {
            Log.v("alarms", "AlarmScheduler.updateSchedule()")
            WorkManager.getInstance(context)
                .enqueue(OneTimeWorkRequest.from(AlarmScheduler::class.java))
        }
    }

    private val alarmManager =
        applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val alarmDao = AppDatabase.getInstance(applicationContext).alarmDao()

    override fun doWork(): Result {
        // Schedule new one
        Log.v("alarms", "AlarmScheduler received work")
        try {
            val scheduledAlarm = getNextAlarm(alarmDao.getAllSync())

            if (scheduledAlarm == null) {
                Log.i("alarms", "No alarms to schedule, canceling pending intent")
                cancelAlarm()
                return Result.success()
            }
            val scheduledAt = scheduledAlarm.first
            val alarm = scheduledAlarm.second
            val pendingIntent = createPendingBroadcastIntent(alarm.id)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val alarmInfo = AlarmManager.AlarmClockInfo(scheduledAt.timeInMillis, pendingIntent)
                alarmManager?.setAlarmClock(alarmInfo, pendingIntent)
            } else {
                TODO("Schedule alarm on lower versions of android")
            }
            Log.i("alarms", "Scheduled $alarm")
        } catch (err: Error) {
            Log.e("alarms", "Failed to setup alarm: $err")
            return Result.failure()
        }

        return Result.success()
    }

    private fun createPendingBroadcastIntent(alarmId: Long? = null): PendingIntent {
        return PendingIntent.getBroadcast(
            applicationContext,
            REQUEST_CODE_NEXT_ALARM,
            AlarmBroadcastReceiver.createIntent(applicationContext, alarmId ?: -1),
            0
        )
    }

    private fun cancelAlarm() {
        alarmManager?.cancel(createPendingBroadcastIntent())
        Log.v("alarms", "AlarmScheduler canceled alarm")
    }

    private fun getNextAlarm(alarms: List<Alarm>?): Pair<Calendar, Alarm>? {
        if (alarms == null) return null

        val now = Calendar.getInstance()
        val scheduledAlarms = TreeSet<Pair<Calendar, Alarm>>() { left, right ->
            if (left.first.timeInMillis < right.first.timeInMillis) -1 else 1
        }
        alarms.forEach { alarm ->
            alarm.getNextOccurrence(now)?.also {
                scheduledAlarms.add(it)
            }
        }
        scheduledAlarms.forEach {
            Log.v("alarms", "${it.first.time}")
        }

        try {
            return scheduledAlarms.first()
        } catch (err: NoSuchElementException) {
            return null
        }
    }
}