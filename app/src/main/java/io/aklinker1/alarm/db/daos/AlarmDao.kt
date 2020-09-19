package io.aklinker1.alarm.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import io.aklinker1.alarm.models.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY time ASC")
    fun getAll(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms ORDER BY time ASC")
    fun getAllSync(): List<Alarm>

    @Query("SELECT * FROM alarms WHERE id=:alarmId")
    suspend fun get(alarmId: Long): Alarm

    @Query("SELECT * FROM alarms WHERE id=:alarmId")
    fun getLive(alarmId: Long): LiveData<Alarm>

    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(user: Alarm)
}
