package io.aklinker1.alarm.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.models.AlarmWithPuzzleCount
import io.aklinker1.alarm.models.Puzzle

@Dao
interface PuzzleDao {
    @Query("SELECT * FROM puzzles WHERE alarm_id=:alarmId ORDER BY created_at ASC")
    fun getForLive(alarmId: Long): LiveData<List<Puzzle>>

    @Query("SELECT * FROM puzzles WHERE id=:alarmId")
    suspend fun get(alarmId: Long): Puzzle

    @Query("SELECT * FROM puzzles WHERE id=:puzzleId")
    fun getLive(puzzleId: Long): LiveData<Puzzle>

    @Insert
    suspend fun insert(puzzle: Puzzle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(puzzle: Puzzle)

    @Delete
    suspend fun delete(puzzle: Puzzle)
}
