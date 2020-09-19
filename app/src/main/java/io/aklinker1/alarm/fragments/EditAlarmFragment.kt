package io.aklinker1.alarm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.aklinker1.alarm.R
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.TimeFormatter
import io.aklinker1.alarm.view_models.EditAlarmViewModel
import kotlinx.coroutines.launch

class EditAlarmFragment : Fragment() {

    private val args: EditAlarmFragmentArgs by navArgs()
    private val viewModel by viewModels<EditAlarmViewModel> {
        EditAlarmViewModel.Factory(requireActivity().application, args.alarm)
    }

    private lateinit var alarmTime: Toolbar
    private lateinit var alarmName: EditText
    private lateinit var sunday: FrameLayout
    private lateinit var monday: FrameLayout
    private lateinit var tuesday: FrameLayout
    private lateinit var wednesday: FrameLayout
    private lateinit var thursday: FrameLayout
    private lateinit var friday: FrameLayout
    private lateinit var saturday: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v("alarm", "Starting with ${args.alarm}")
        return inflater.inflate(R.layout.fragment_edit_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        alarmTime = view.findViewById(R.id.alarm_time)
        alarmName = view.findViewById(R.id.alarm_name)
        sunday = view.findViewById(R.id.sunday)
        monday = view.findViewById(R.id.monday)
        tuesday = view.findViewById(R.id.tuesday)
        wednesday = view.findViewById(R.id.wednesday)
        thursday = view.findViewById(R.id.thursday)
        friday = view.findViewById(R.id.friday)
        saturday = view.findViewById(R.id.saturday)

        alarmTime.setupWithNavController(navController, AppBarConfiguration(navController.graph))

        viewModel.alarm.observe(requireActivity(), {
            alarmTime.title = TimeFormatter.alarmTime(it.time)
            alarmName.setText(it.name ?: "Untitled Alarm")

            sunday.isSelected = it.repeatSunday
            monday.isSelected = it.repeatMonday
            tuesday.isSelected = it.repeatTuesday
            wednesday.isSelected = it.repeatWednesday
            thursday.isSelected = it.repeatThursday
            friday.isSelected = it.repeatFriday
            saturday.isSelected = it.repeatSaturday
        })

        sunday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatSunday = !it.isSelected))
        }
        monday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatMonday = !it.isSelected))
        }
        tuesday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatTuesday = !it.isSelected))
        }
        wednesday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatWednesday = !it.isSelected))
        }
        thursday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatThursday = !it.isSelected))
        }
        friday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatFriday = !it.isSelected))
        }
        saturday.setOnClickListener {
            updateAlarmWeekdays(viewModel.alarm.value?.copy(repeatSaturday = !it.isSelected))
        }
    }

    private fun updateAlarmWeekdays(newAlarm: Alarm?) {
        val alarm = newAlarm ?: return

        this.lifecycleScope.launch {
            viewModel.updateAlarm(alarm)
        }
    }
}