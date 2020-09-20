package io.aklinker1.alarm.adapters.view_holders

interface AlarmListItemClickListener {
    fun onClickAlarm(alarmId: Long)
    fun onLongClickAlarm(alarmId: Long)
    fun onClickAlarmTime(alarmId: Long)
    fun onToggleAlarm(alarmId: Long, newIsChecked: Boolean)
}