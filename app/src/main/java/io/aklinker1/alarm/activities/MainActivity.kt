package io.aklinker1.alarm.activities

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.aklinker1.alarm.R
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.DateUtils
import io.aklinker1.alarm.view_models.AlarmListViewModel
import io.aklinker1.alarm.workers.AlarmScheduler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener(this.onClickFab)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(onClickFab)
    }

    private val onClickFab = fun(_: View) {
        val alarm = Alarm(null, DateUtils.dateAt(6), true)
        lifecycleScope.launch {
            alarmListViewModel.createAlarm(alarm)
            Log.v("alarms", alarm.toString())
            AlarmScheduler.updateAlarm(this@MainActivity, alarm)
        }
    }
}