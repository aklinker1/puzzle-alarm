package io.aklinker1.alarm.adapters

import io.aklinker1.alarm.models.Alarm

interface AlarmListItemClickListener {
    fun onClickAlarm(alarmId: Long)
    fun onClickAlarmTime(alarmId: Long)
    fun onToggleAlarm(alarmId: Long, newIsChecked: Boolean)
}