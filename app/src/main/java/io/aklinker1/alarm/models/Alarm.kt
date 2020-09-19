package io.aklinker1.alarm.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "alarms")
data class Alarm(
    @ColumnInfo var name: String?,
    @ColumnInfo var time: Calendar,
    @ColumnInfo var enabled: Boolean,
    @ColumnInfo(name = "repeat_sunday") var repeatSunday: Boolean = false,
    @ColumnInfo(name = "repeat_monday") var repeatMonday: Boolean = false,
    @ColumnInfo(name = "repeat_tuesday") var repeatTuesday: Boolean = false,
    @ColumnInfo(name = "repeat_wednesday") var repeatWednesday: Boolean = false,
    @ColumnInfo(name = "repeat_thursday") var repeatThursday: Boolean = false,
    @ColumnInfo(name = "repeat_friday") var repeatFriday: Boolean = false,
    @ColumnInfo(name = "repeat_saturday") var repeatSaturday: Boolean = false,
    @ColumnInfo @PrimaryKey(autoGenerate = true) var id: Long = 0L
) : Serializable {
    val repeatsText: String
        get (): String {
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

    companion object {
        val DIFF_CALLBACK= object : DiffUtil.ItemCallback<Alarm>() {
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun toString(): String {
        return "$name (id=$id)"
    }
}
