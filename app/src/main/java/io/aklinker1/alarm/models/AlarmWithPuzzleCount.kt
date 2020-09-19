package io.aklinker1.alarm.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Embedded

data class AlarmWithPuzzleCount(@Embedded val alarm: Alarm, @ColumnInfo(name = "puzzle_count") val puzzleCount: Int) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlarmWithPuzzleCount>() {
            override fun areItemsTheSame(oldItem: AlarmWithPuzzleCount, newItem: AlarmWithPuzzleCount): Boolean {
                return oldItem.alarm.id == newItem.alarm.id
            }

            override fun areContentsTheSame(oldItem: AlarmWithPuzzleCount, newItem: AlarmWithPuzzleCount): Boolean {
                return oldItem == newItem
            }
        }
    }

}