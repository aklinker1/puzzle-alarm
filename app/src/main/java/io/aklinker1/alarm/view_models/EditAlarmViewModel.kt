package io.aklinker1.alarm.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.db.repositories.AlarmRepository
import io.aklinker1.alarm.models.Alarm

class EditAlarmViewModel(application: Application, alarm: Alarm) : AndroidViewModel(application) {

    class Factory(private val application: Application, private val alarm: Alarm) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return EditAlarmViewModel(application, alarm) as T
        }
    }

    private val database = AppDatabase.getInstance(application)
    private val alarmRepository = AlarmRepository(database)

    val alarm = alarmRepository.getLive(alarm.id)

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.update(alarm)
    }

}