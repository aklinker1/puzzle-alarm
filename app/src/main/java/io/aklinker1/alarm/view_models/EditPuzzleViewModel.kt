package io.aklinker1.alarm.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aklinker1.alarm.db.AppDatabase
import io.aklinker1.alarm.models.Puzzle
import io.aklinker1.puzzle.db.repositories.PuzzleRepository

class EditPuzzleViewModel(application: Application, puzzle: Puzzle) :
    AndroidViewModel(application) {

    class Factory(private val application: Application, private val puzzle: Puzzle) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return EditPuzzleViewModel(application, puzzle) as T
        }
    }

    private val database = AppDatabase.getInstance(application)
    private val puzzleRepository = PuzzleRepository(database)

    val puzzle = puzzleRepository.getLive(puzzle.id)

    suspend fun updatePuzzle(puzzle: Puzzle) {
        puzzleRepository.update(puzzle)
    }

    suspend fun getPuzzle(puzzleId: Long): Puzzle {
        return puzzleRepository.get(puzzleId)
    }

}
