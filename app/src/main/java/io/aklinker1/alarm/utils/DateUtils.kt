package io.aklinker1.alarm.utils

import java.util.*

object DateUtils {

    fun dateAt(hours: Int, minutes: Int = 0, seconds: Int = 0, base: Calendar? = null): Calendar {
        val result = if (base != null) base.clone() as Calendar else Calendar.getInstance()

        result.set(Calendar.HOUR_OF_DAY, hours)
        result.set(Calendar.MINUTE, minutes)
        result.set(Calendar.SECOND, seconds)

        return result
    }

    fun createAlarmDate(input: Calendar, hours: Int, minutes: Int): Calendar {
        val newCalendar = input.clone() as Calendar

        newCalendar[Calendar.HOUR_OF_DAY] = hours
        newCalendar[Calendar.MINUTE] = minutes
        newCalendar[Calendar.SECOND] = 0
        newCalendar[Calendar.MILLISECOND] = 0

        return newCalendar
    }
}