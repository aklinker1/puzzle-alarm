package io.aklinker1.alarm.adapters.view_holders

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.AlarmListItemClickListener
import io.aklinker1.alarm.models.Alarm
import java.text.SimpleDateFormat
import java.util.*

class AlarmViewHolder(private val clickHandler: AlarmListItemClickListener, view: View) : RecyclerView.ViewHolder(view) {
    private val click: View = view.findViewById(R.id.click)
    private val name: TextView = view.findViewById(R.id.name)
    private val time: TextView = view.findViewById(R.id.time)
    private val repeats: TextView = view.findViewById(R.id.repeats)
    private val toggle: SwitchCompat = view.findViewById(R.id.toggle)

    fun bind(alarm: Alarm, index: Int) {
        val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
        name.text = alarm.name ?: "Alarm"
        time.text = timeFormatter.format(alarm.time).toLowerCase(Locale.getDefault())
        toggle.isChecked = alarm.enabled
        time.alpha = if (!alarm.enabled) 0.6f else 1f
        repeats.text = alarm.repeatsText

        click.setOnClickListener {
            clickHandler.onClickAlarm(index)
        }
        time.setOnClickListener {
            clickHandler.onClickAlarmTime(index)
        }
        toggle.setOnCheckedChangeListener { _, newIsChecked ->
            clickHandler.onToggleAlarm(index, newIsChecked)
        }
    }
}