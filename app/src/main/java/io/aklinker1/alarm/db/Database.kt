package io.aklinker1.alarm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.aklinker1.alarm.db.converters.DateConverter
import io.aklinker1.alarm.db.daos.AlarmDao
import io.aklinker1.alarm.db.daos.PuzzleDao
import io.aklinker1.alarm.models.*
import io.aklinker1.alarm.utils.SingletonHolder


@Database(entities = [Alarm::class, Puzzle::class], version = 1)
@TypeConverters(value = [DateConverter::class, AlarmTimeConverter::class, PuzzleTypeConverter::class, PuzzleCategoryConverter::class, ShapeComplexityConverter::class])
abstract class AppDatabase : RoomDatabase() {

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java,
            "alarms_database"
        ).build()
    })

    abstract fun alarmDao(): AlarmDao
    abstract fun puzzleDao(): PuzzleDao

}
