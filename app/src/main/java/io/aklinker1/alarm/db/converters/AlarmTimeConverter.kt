package io.aklinker1.alarm.db.converters

import androidx.room.TypeConverter
import io.aklinker1.alarm.models.AlarmTime
import io.aklinker1.alarm.models.hours
import io.aklinker1.alarm.models.minutes

class AlarmTimeConverter {
    @TypeConverter
    fun fromMinutes(value: Int?): AlarmTime? {
        if (value == null) return null
        val hours = value / 60
        val minutes = value - hours * 60
        return AlarmTime(hours, minutes)
    }

    @TypeConverter
    fun timeToMinutes(value: AlarmTime?): Int? {
        if (value == null) return null
        return value.hours * 60 + value.minutes
    }
}