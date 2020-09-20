package io.aklinker1.alarm.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.adapters.EditAlarmAdapter
import io.aklinker1.alarm.adapters.view_holders.AddPuzzleListItemClickListener
import io.aklinker1.alarm.adapters.view_holders.PuzzleListItemClickListener
import io.aklinker1.alarm.adapters.view_holders.WeekdaySelectorListItemClickListener
import io.aklinker1.alarm.fragments.edit_puzzles.EditPuzzleGridHighlightArgs
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.models.Puzzle
import io.aklinker1.alarm.utils.TimeFormatter
import io.aklinker1.alarm.view_models.EditAlarmViewModel
import io.aklinker1.alarm.workers.AlarmScheduler
import kotlinx.coroutines.launch

class EditAlarmFragment : Fragment(), WeekdaySelectorListItemClickListener,
    PuzzleListItemClickListener, AddPuzzleListItemClickListener {

    private val args: EditAlarmFragmentArgs by navArgs()
    private val viewModel by viewModels<EditAlarmViewModel> {
        EditAlarmViewModel.Factory(requireActivity().application, args.alarm)
    }
    val puzzles = ArrayList<Puzzle>()

    private lateinit var list: RecyclerView
    private lateinit var alarmName: EditText
    private lateinit var alarmTime: Toolbar
    private lateinit var alarmEnabled: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        list = view.findViewById(R.id.recycler_view)
        alarmName = view.findViewById(R.id.alarm_name)
        alarmTime = view.findViewById(R.id.alarm_time)
        alarmEnabled = view.findViewById(R.id.alarm_enabled)
        list = view.findViewById(R.id.recycler_view)

        list.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EditAlarmAdapter(this, this, this)
        list.adapter = adapter

        alarmTime.setupWithNavController(navController, AppBarConfiguration(navController.graph))

        viewModel.alarm.observe(requireActivity(), {
            alarmTime.title = TimeFormatter.alarmTime(it.time)

            alarmEnabled.isChecked = it.enabled

            alarmName.setText(it.name ?: "Untitled Alarm")
            alarmName.setTypeface(null, if (it.name == null) Typeface.ITALIC else Typeface.NORMAL)

            adapter.submitList(it, viewModel.puzzles.value)
        })
        viewModel.puzzles.observe(requireActivity(), {
            adapter.submitList(viewModel.alarm.value, it)
        })

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
            AlarmScheduler.updateSchedule(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.alarm.removeObservers(requireActivity())
    }

    private fun saveAlarmHelper(newAlarm: Alarm?) {
        val alarm = newAlarm ?: return

        this.lifecycleScope.launch {
            viewModel.updateAlarm(alarm)
        }
    }

    override fun onClickDay(dayOfWeek: Int, repeated: Boolean) {
        val newAlarm = viewModel.alarm.value?.copy()?.run {
            when (dayOfWeek) {
                0 -> this.repeatSunday = repeated
                1 -> this.repeatMonday = repeated
                2 -> this.repeatTuesday = repeated
                3 -> this.repeatWednesday = repeated
                4 -> this.repeatThursday = repeated
                5 -> this.repeatFriday = repeated
                6 -> this.repeatSaturday = repeated
            }
            this
        }
        saveAlarmHelper(newAlarm)
    }

    override fun onClickItem(puzzleId: Long) {
        lifecycleScope.launch {
            val puzzle = viewModel.getPuzzle(puzzleId)
            val args = EditPuzzleGridHighlightArgs(puzzle)
            findNavController().navigate(R.id.action_EditAlarm_to_EditPuzzle, args.toBundle())
        }
    }

    override fun onClickDeleteItem(puzzleId: Long) {
        lifecycleScope.launch {
            val puzzle = viewModel.getPuzzle(puzzleId)
            viewModel.deletePuzzle(puzzle)
        }
    }

    override fun onClickAddPuzzle() {
        lifecycleScope.launch {
            viewModel.createPuzzle(Puzzle.MemoryGridHighlights(args.alarm.id))
        }
    }

}