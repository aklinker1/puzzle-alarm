package io.aklinker1.alarm.utils

import java.util.*
import kotlin.math.min

object DateUtils {

    fun dateAt(hours: Int, minutes: Int = 0, seconds: Int = 0): Date {
        val date = Date()
        date.hours = hours
        date.minutes = minutes
        date.seconds = seconds
        return date
    }
}