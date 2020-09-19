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
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.AlarmListAdapter
import io.aklinker1.alarm.adapters.AlarmListItemClickListener
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.DateUtils
import io.aklinker1.alarm.view_models.AlarmListViewModel
import io.aklinker1.alarm.workers.AlarmScheduler
import kotlinx.android.synthetic.main.fragment_alarm_list.*
import kotlinx.coroutines.launch
import java.util.*

class AlarmListFragment : Fragment(), AlarmListItemClickListener {

    private lateinit var adapter: AlarmListAdapter
    private lateinit var listView: RecyclerView
    private val alarmListViewModel: AlarmListViewModel by viewModels()

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

        fab.setOnClickListener(this.onClickFab)
    }

    // List item handlers

    override fun onClickAlarm(alarmId: Long) {
        lifecycleScope.launch {
            val alarm = alarmListViewModel.getAlarm(alarmId)
            val args = EditAlarmFragmentArgs(alarm)
            findNavController().navigate(R.id.action_AlarmList_to_EditAlarm, args.toBundle())
        }
    }

    override fun onClickAlarmTime(alarmId: Long) {
        lifecycleScope.launch {
            val alarm = alarmListViewModel.getAlarm(alarmId)
            val hour = alarm.time[Calendar.HOUR_OF_DAY]
            val minutes = alarm.time[Calendar.MINUTE]
            TimePickerDialog(
                this@AlarmListFragment.context,
                onSelectTimeForAlarm(alarm),
                hour,
                minutes,
                false
            ).show()
        }
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

    override fun onToggleAlarm(alarmId: Long, newIsChecked: Boolean) {
        this.lifecycleScope.launch {
            val alarm = alarmListViewModel.getAlarm(alarmId)
            val newAlarm = alarm.copy(enabled = newIsChecked)
            alarmListViewModel.updateAlarm(newAlarm)
            AlarmScheduler.updateAlarm(requireContext(), alarm)
        }
    }

    private val onClickFab = fun(_: View) {
        val alarm = Alarm(null, DateUtils.dateAt(6), true)
        lifecycleScope.launch {
            alarmListViewModel.createAlarm(alarm)
            Log.v("alarms", alarm.toString())
            AlarmScheduler.updateAlarm(requireContext(), alarm)
        }
    }
}