package io.aklinker1.alarm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aklinker1.alarm.R
import io.aklinker1.alarm.utils.turnScreenOffAndKeyguardOn
import io.aklinker1.alarm.utils.turnScreenOnAndKeyguardOff

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        turnScreenOnAndKeyguardOff()
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}