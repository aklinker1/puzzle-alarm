package io.aklinker1.alarm.db.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        return if (value == null) null else with(Calendar.getInstance()) { timeInMillis = value; this }
    }

    @TypeConverter
    fun dateToTimestamp(value: Calendar?): Long? {
        return value?.timeInMillis
    }
}