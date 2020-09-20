package io.aklinker1.alarm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.view_holders.AlarmListItemClickListener
import io.aklinker1.alarm.adapters.view_holders.AlarmListItemViewHolder
import io.aklinker1.alarm.models.AlarmWithPuzzleCount

class AlarmListAdapter(private val clickHandler: AlarmListItemClickListener) :
    ListAdapter<AlarmWithPuzzleCount, AlarmListItemViewHolder>(AlarmWithPuzzleCount.DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: AlarmListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListItemViewHolder {
        return AlarmListItemViewHolder(
            clickHandler,
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_alarm, parent, false)
        )
    }
}