package io.aklinker1.alarm.adapters.view_holders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.AlarmListItemClickListener
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.TimeFormatter

class AlarmViewHolder(private val clickHandler: AlarmListItemClickListener, view: View) :
    RecyclerView.ViewHolder(view) {
    private val card: View = view.findViewById(R.id.card)
    private val border: View = view.findViewById(R.id.border)
    private val click: View = view.findViewById(R.id.click)
    private val name: TextView = view.findViewById(R.id.name)
    private val time: TextView = view.findViewById(R.id.time)
    private val repeats: TextView = view.findViewById(R.id.repeats)
    private val toggle: SwitchCompat = view.findViewById(R.id.toggle)

    fun bind(alarm: Alarm) {
        name.text = alarm.name ?: "Alarm"
        time.text = TimeFormatter.alarmTime(alarm.time)
        repeats.text = alarm.repeatsText

        toggle.isChecked = alarm.enabled
        card.isSelected = alarm.enabled
        border.isSelected = alarm.enabled
        time.alpha = if (alarm.enabled) 1f else 0.64f

        click.setOnClickListener {
            clickHandler.onClickAlarm(alarm.id)
        }
        time.setOnClickListener {
            clickHandler.onClickAlarmTime(alarm.id)
        }
        toggle.setOnCheckedChangeListener { _, newIsChecked ->
            clickHandler.onToggleAlarm(alarm.id, newIsChecked)
        }
    }
}