package io.aklinker1.alarm.models

import androidx.room.TypeConverter

typealias AlarmTime = Pair<Int, Int>
val AlarmTime.hours
    get() = this.first
val AlarmTime.minutes
    get() = this.second

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