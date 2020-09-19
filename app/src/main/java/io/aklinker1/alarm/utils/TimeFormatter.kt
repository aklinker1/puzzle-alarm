package io.aklinker1.alarm.utils

import io.aklinker1.alarm.models.AlarmTime
import io.aklinker1.alarm.models.hours
import io.aklinker1.alarm.models.minutes
import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    fun alarmTime(alarmTime: AlarmTime): String {
        val locale = Locale.getDefault()
        val formatted = SimpleDateFormat("h:mm a", locale).format(
            DateUtils.dateAt(
                hours = alarmTime.hours,
                minutes = alarmTime.minutes
            ).time
        )
        return formatted.toLowerCase(locale)
    }
}