package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class PuzzleCategory {
    Memory;

    companion object {
        private const val MEMORY = 0
    }

    @TypeConverter
    fun databaseToEnum(value: Int?): PuzzleCategory? {
        return when(value) {
            MEMORY -> Memory
            else -> null
        }
    }

    @TypeConverter
    fun enumToDatabase(value: PuzzleCategory?): Int? {
        return when(value) {
            Memory -> MEMORY
            else -> null
        }
    }
}