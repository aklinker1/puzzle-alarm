package io.aklinker1.alarm.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.db.repositories.AlarmRepository
import io.aklinker1.alarm.models.Alarm

class AlarmListViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val alarmRepository = AlarmRepository(database)
    val alarms = alarmRepository.alarmList

    suspend fun createAlarm(alarm: Alarm) {
        alarmRepository.create(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.update(alarm)
    }
}