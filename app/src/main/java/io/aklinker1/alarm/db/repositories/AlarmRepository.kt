package io.aklinker1.alarm.db.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.db.daos.AlarmDao
import io.aklinker1.alarm.models.Alarm

class AlarmRepository(val database: AppDatabase) {

    private val alarmDao = database.alarmDao()
    val alarmList = alarmDao.getAll()

    suspend fun create(alarm: Alarm) {
            alarm.id = alarmDao.insert(alarm)
    }

    suspend fun update(alarm: Alarm) {
            alarmDao.update(alarm)
    }
}