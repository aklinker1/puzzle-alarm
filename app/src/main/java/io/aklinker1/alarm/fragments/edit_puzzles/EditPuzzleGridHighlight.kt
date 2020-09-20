package io.aklinker1.alarm.fragments.edit_puzzles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.aklinker1.alarm.R
import io.aklinker1.alarm.view_models.EditPuzzleViewModel
import io.aklinker1.alarm.views.GridHighlightPuzzleView
import kotlinx.coroutines.launch

class EditPuzzleGridHighlight : Fragment() {

    companion object {
        const val MIN_GRID_SIZE = 3
        const val MIN_HIGHLIGHT_COUNT = 1
    }

    private val args: EditPuzzleGridHighlightArgs by navArgs()
    private val viewModel by viewModels<EditPuzzleViewModel> {
        EditPuzzleViewModel.Factory(requireActivity().application, args.puzzle)
    }

    private lateinit var toolbar: Toolbar
    private lateinit var reset: Button
    private lateinit var grid: GridHighlightPuzzleView
    private lateinit var gridSize: TextView
    private lateinit var gridSizeSlider: SeekBar
    private lateinit var highlightCount: TextView
    private lateinit var highlightCountSlider: SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_puzzle_grid_highlight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        reset = view.findViewById(R.id.reset)
        grid = view.findViewById(R.id.grid)
        gridSize = view.findViewById(R.id.grid_size)
        gridSizeSlider = view.findViewById(R.id.grid_size_slider)
        highlightCount = view.findViewById(R.id.highlight_count)
        highlightCountSlider = view.findViewById(R.id.highlight_count_slider)

        val navController = findNavController()
        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))

        viewModel.puzzle.observe(requireActivity(), {
            val gridSizeValue = it.gridSize ?: 5
            gridSize.text = "$gridSizeValue"
            gridSizeSlider.progress = gridSizeValue - MIN_GRID_SIZE
            grid.gridSize = gridSizeValue

            val maxHighlightCount: Int = gridSizeValue * gridSizeValue / 2
            var highlightCountValue = it.gridCount ?: 7
            if (highlightCountValue > maxHighlightCount) highlightCountValue = maxHighlightCount
            highlightCountSlider.max = maxHighlightCount - MIN_HIGHLIGHT_COUNT
            highlightCount.text = "$highlightCountValue"
            highlightCountSlider.progress = highlightCountValue - MIN_HIGHLIGHT_COUNT
            grid.highlightCount = highlightCountValue

            grid.visibility = View.VISIBLE
        })

        gridSizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateText(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateText(gridSizeSlider.progress)
                updateData(gridSizeSlider.progress)
            }

            fun updateText(progress: Int) {
                val value = progress + MIN_GRID_SIZE
                gridSize.text = "$value"
            }

            fun updateData(progress: Int) {
                lifecycleScope.launch {
                    val value = progress + MIN_GRID_SIZE
                    val newPuzzle = viewModel.getPuzzle(args.puzzle.id).copy(gridSize = value)
                    viewModel.updatePuzzle(newPuzzle)
                }
            }
        })

        highlightCountSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateText(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateText(highlightCountSlider.progress)
                updateData(highlightCountSlider.progress)
            }

            fun updateText(progress: Int) {
                val value = progress + MIN_HIGHLIGHT_COUNT
                highlightCount.text = "$value"
            }

            fun updateData(progress: Int) {
                lifecycleScope.launch {
                    val value = progress + MIN_HIGHLIGHT_COUNT
                    val newPuzzle = viewModel.getPuzzle(args.puzzle.id).copy(gridCount = value)
                    viewModel.updatePuzzle(newPuzzle)
                }
            }
        })

        reset.setOnClickListener {
            grid.reset()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.puzzle.removeObservers(requireActivity())
    }

}