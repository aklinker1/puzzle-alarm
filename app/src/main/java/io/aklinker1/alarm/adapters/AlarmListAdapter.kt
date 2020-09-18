package io.aklinker1.alarm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.view_holders.AlarmViewHolder
import io.aklinker1.alarm.models.Alarm

class AlarmListAdapter(private val clickHandler: AlarmListItemClickListener) :
    ListAdapter<Alarm, AlarmViewHolder>(Alarm.DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            clickHandler,
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_alarm, parent, false)
        )
    }
}