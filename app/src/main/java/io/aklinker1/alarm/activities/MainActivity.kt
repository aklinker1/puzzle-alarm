package io.aklinker1.alarm.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.aklinker1.alarm.R
import io.aklinker1.alarm.view_models.AlarmListViewModel

class MainActivity : AppCompatActivity() {

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


    }

}