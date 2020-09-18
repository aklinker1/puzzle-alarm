package io.aklinker1.alarm.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.AlarmListAdapter
import io.aklinker1.alarm.adapters.AlarmListItemClickListener
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.DateUtils
import io.aklinker1.alarm.view_models.AlarmListViewModel
import io.aklinker1.alarm.workers.AlarmScheduler
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlarmListFragment : Fragment(), AlarmListItemClickListener {

    private lateinit var adapter: AlarmListAdapter
    private lateinit var listView: RecyclerView
    private val alarmListViewModel: AlarmListViewModel by viewModels()
    private lateinit var workManager: WorkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.alarm_list)

        adapter = AlarmListAdapter(this)
        alarmListViewModel.alarms.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        listView.apply {
            this.adapter = this@AlarmListFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    // List item handlers

    override fun onClickAlarm(index: Int) {
        val alarm = adapter.currentList[index]
        Log.d("alarms", "Clicked: $alarm")
    }

    override fun onClickAlarmTime(index: Int) {
        val alarm = adapter.currentList[index]
        val hour = alarm.time.hours
        val minutes = alarm.time.minutes
        TimePickerDialog(this.context, onSelectTimeForAlarm(alarm), hour, minutes, false).show()
    }

    private fun onSelectTimeForAlarm(alarm: Alarm): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { _, hours, minutes ->
            Log.v("alarms", "Updating $alarm to $hours:$minutes")
            val newAlarm = alarm.copy(time = DateUtils.dateAt(hours, minutes))
            this.lifecycleScope.launch {
                AlarmScheduler.updateAlarm(requireContext(), newAlarm)
                alarmListViewModel.updateAlarm(newAlarm)
            }
        }
    }

    override fun onToggleAlarm(index: Int, newIsChecked: Boolean) {
        val alarm = adapter.currentList[index]
        this.lifecycleScope.launch {
            val newAlarm = alarm.copy(enabled = newIsChecked)
            alarmListViewModel.updateAlarm(newAlarm)
            AlarmScheduler.updateAlarm(requireContext(), alarm)
        }
    }
}