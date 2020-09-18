package io.aklinker1.alarm.db

import android.content.Context
import android.util.Log
import androidx.room.*
import io.aklinker1.alarm.db.converters.DateConverter
import io.aklinker1.alarm.db.daos.AlarmDao
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.SingletonHolder


@Database(entities = [Alarm::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java,
            "alarms_database"
        ).build()
    })

    abstract fun alarmDao(): AlarmDao
}
