package io.aklinker1.alarm.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.SwitchCompat
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
import io.aklinker1.alarm.utils.KeyboardUtils
import io.aklinker1.alarm.utils.TimeFormatter
import io.aklinker1.alarm.view_models.EditAlarmViewModel
import kotlinx.coroutines.launch

class EditAlarmFragment : Fragment() {

    private val args: EditAlarmFragmentArgs by navArgs()
    private val viewModel by viewModels<EditAlarmViewModel> {
        EditAlarmViewModel.Factory(requireActivity().application, args.alarm)
    }

    private lateinit var alarmName: EditText
    private lateinit var alarmTime: Toolbar
    private lateinit var alarmEnabled: SwitchCompat
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
        alarmName = view.findViewById(R.id.alarm_name)
        alarmTime = view.findViewById(R.id.alarm_time)
        alarmEnabled = view.findViewById(R.id.alarm_enabled)
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

            alarmEnabled.isChecked = it.enabled

            alarmName.setText(it.name ?: "Untitled Alarm")
            alarmName.setTypeface(null, if (it.name == null) Typeface.ITALIC else Typeface.NORMAL)

            sunday.isSelected = it.repeatSunday
            monday.isSelected = it.repeatMonday
            tuesday.isSelected = it.repeatTuesday
            wednesday.isSelected = it.repeatWednesday
            thursday.isSelected = it.repeatThursday
            friday.isSelected = it.repeatFriday
            saturday.isSelected = it.repeatSaturday
        })

        sunday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatSunday = !it.isSelected))
        }
        monday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatMonday = !it.isSelected))
        }
        tuesday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatTuesday = !it.isSelected))
        }
        wednesday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatWednesday = !it.isSelected))
        }
        thursday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatThursday = !it.isSelected))
        }
        friday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatFriday = !it.isSelected))
        }
        saturday.setOnClickListener {
            saveAlarmHelper(viewModel.alarm.value?.copy(repeatSaturday = !it.isSelected))
        }

        alarmName.setSelectAllOnFocus(true)
        alarmName.setOnEditorActionListener { input, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    var newName = input.text.toString().trim().run {
                        if (this == "") return@run null
                        if (this == "Untitled Alarm" && viewModel.alarm.value?.name == null) return@run null
                        this
                    }
                    saveAlarmHelper(viewModel.alarm.value?.copy(name = newName))
                    alarmName.isCursorVisible = false
                    false // hide keyboard
                }
                else -> false
            }
        }
        alarmName.setOnClickListener { alarmName.isCursorVisible = true }

        alarmEnabled.setOnCheckedChangeListener { _, isChecked ->
            saveAlarmHelper(viewModel.alarm.value?.copy(enabled = isChecked))
        }
    }

    private fun saveAlarmHelper(newAlarm: Alarm?) {
        val alarm = newAlarm ?: return

        this.lifecycleScope.launch {
            viewModel.updateAlarm(alarm)
        }
    }
}