package io.aklinker1.alarm.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    fun alarmTime(time: Calendar): String {
        val locale = Locale.getDefault()
        val formatted =  SimpleDateFormat("h:mm a", locale).format(time.time)
        return formatted.toLowerCase(locale)
    }
}