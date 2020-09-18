package io.aklinker1.alarm.utils

import java.util.*
import kotlin.math.min

object DateUtils {

    fun dateAt(hours: Int, minutes: Int = 0, seconds: Int = 0): Calendar {
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, hours)
        date.set(Calendar.MINUTE, minutes)
        date.set(Calendar.SECOND, seconds)
        return date
    }
}