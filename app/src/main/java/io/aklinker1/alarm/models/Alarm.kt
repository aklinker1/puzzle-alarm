package io.aklinker1.alarm.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.aklinker1.alarm.utils.DateUtils
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "alarms")
data class Alarm(
    @ColumnInfo var name: String?,
    @ColumnInfo var time: AlarmTime,
    @ColumnInfo var enabled: Boolean,

    @ColumnInfo(name = "repeat_sunday") var repeatSunday: Boolean = false,
    @ColumnInfo(name = "repeat_monday") var repeatMonday: Boolean = false,
    @ColumnInfo(name = "repeat_tuesday") var repeatTuesday: Boolean = false,
    @ColumnInfo(name = "repeat_wednesday") var repeatWednesday: Boolean = false,
    @ColumnInfo(name = "repeat_thursday") var repeatThursday: Boolean = false,
    @ColumnInfo(name = "repeat_friday") var repeatFriday: Boolean = false,
    @ColumnInfo(name = "repeat_saturday") var repeatSaturday: Boolean = false,
    @ColumnInfo(name = "created_at") var createdAt: Calendar = Calendar.getInstance(),

    @ColumnInfo @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Serializable {

    val repeatsText: String
        get(): String {
            val list = ArrayList<String>(7)
            if (repeatSunday) list.add("Sun")
            if (repeatMonday) list.add("Mon")
            if (repeatTuesday) list.add("Tue")
            if (repeatWednesday) list.add("Wed")
            if (repeatThursday) list.add("Thu")
            if (repeatFriday) list.add("Fri")
            if (repeatSaturday) list.add("Sat")

            if (list.size == 7) return "Everyday"
            if (list.size == 2 && repeatSunday && repeatSaturday) return "Weekend"
            if (list.size == 5 && !repeatSunday && !repeatSaturday) return "Weekdays"

            return if (list.size == 0) "Not Repeated" else list.joinToString()
        }
    val isRepeated: Boolean
        get() = repeatSunday || repeatMonday || repeatTuesday || repeatWednesday || repeatThursday || repeatFriday || repeatSaturday

    fun isRepeatedFor(dayOfWeek: Int): Boolean = when (dayOfWeek) {
        0 -> repeatSunday
        1 -> repeatMonday
        2 -> repeatTuesday
        3 -> repeatWednesday
        4 -> repeatThursday
        5 -> repeatFriday
        6 -> repeatSaturday
        else -> false
    }

    fun getNextOccurrence(base: Calendar?): Pair<Calendar, Alarm>? {
        if (!enabled) return null
        val now = base ?: Calendar.getInstance()

        if (isRepeated) return getNextRepeatedOccurrence(now)

        val today = DateUtils.dateAt(hours = time.first, minutes = time.second, base = now)
        if (today.timeInMillis > now.timeInMillis) return Pair(today, this)

        val tomorrow = today.clone() as Calendar
        tomorrow.add(Calendar.DATE, 1)
        return Pair(tomorrow, this)
    }

    private fun getNextRepeatedOccurrence(now: Calendar): Pair<Calendar, Alarm>? {
        for (i in 0..6) {
            val dayOfWeek =
                (now[Calendar.DAY_OF_WEEK] - 1 + i) % 7 // Calendar.DAY_OF_WEEK returns 1-7, not 0-6
            if (isRepeatedFor(dayOfWeek)) {
                val scheduledAt = now.clone() as Calendar
                scheduledAt.add(Calendar.DATE, i)
                if (scheduledAt.timeInMillis > now.timeInMillis) {
                    return Pair(
                        DateUtils.createAlarmDate(
                            scheduledAt,
                            hours = time.first,
                            minutes = time.second
                        ), this
                    )
                }
            }
        }
        return null
    }

    override fun toString(): String {
        return "Alarm(name='$name', id=$id, time=${time.first}:${if (time.second < 10) "0" + time.second else time.second}, repeats=$repeatsText)"
    }
}
