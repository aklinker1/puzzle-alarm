package io.aklinker1.alarm.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.db.repositories.AlarmRepository
import io.aklinker1.alarm.models.Alarm

class AlarmListViewModel(val app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)
    private val alarmRepository = AlarmRepository(database)
    val alarms = alarmRepository.alarmList

    suspend fun getAlarm(id: Long): Alarm {
        return alarmRepository.get(id);
    }

    suspend fun createAlarm(alarm: Alarm) {
        alarmRepository.create(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.update(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmRepository.delete(alarm)
    }
}