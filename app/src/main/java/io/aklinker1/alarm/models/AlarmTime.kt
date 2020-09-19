package io.aklinker1.alarm.models

typealias AlarmTime = Pair<Int, Int>

val AlarmTime.hours
    get() = this.first

val AlarmTime.minutes
    get() = this.second