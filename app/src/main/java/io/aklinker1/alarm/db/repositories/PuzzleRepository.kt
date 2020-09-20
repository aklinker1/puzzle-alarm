package io.aklinker1.puzzle.db.repositories

import androidx.lifecycle.LiveData
import io.aklinker1.alarm.models.Puzzle
import io.aklinker1.alarm.db.AppDatabase

class PuzzleRepository(database: AppDatabase) {

    private val puzzleDao = database.puzzleDao()

    fun getForLive(alarmId: Long): LiveData<List<Puzzle>> {
        return puzzleDao.getForLive(alarmId)
    }

    suspend fun get(id: Long): Puzzle {
        return puzzleDao.get(id)
    }

    fun getLive(id: Long): LiveData<Puzzle> {
        return puzzleDao.getLive(id)
    }

    suspend fun create(puzzle: Puzzle) {
        puzzle.id = puzzleDao.insert(puzzle)
    }

    suspend fun update(puzzle: Puzzle) {
        puzzleDao.update(puzzle)
    }

    suspend fun delete(puzzle: Puzzle) {
        puzzleDao.delete(puzzle)
    }
}