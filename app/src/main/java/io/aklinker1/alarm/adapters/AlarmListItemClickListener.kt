package io.aklinker1.alarm.adapters

import io.aklinker1.alarm.models.Alarm

interface AlarmListItemClickListener {
    fun onClickAlarm(index: Int)
    fun onClickAlarmTime(index: Int)
    fun onToggleAlarm(index: Int, newIsChecked: Boolean)
}