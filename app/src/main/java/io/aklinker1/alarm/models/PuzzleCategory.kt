package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class PuzzleCategory(val value: Int) {
    Memory(0);

    companion object {
        fun fromValue(value: Int?): PuzzleCategory? {
            return when (value) {
                0 -> Memory
                else -> null
            }
        }
    }
}

class PuzzleCategoryConverter {
    @TypeConverter
    fun databaseToEnum(value: Int?): PuzzleCategory? {
        return PuzzleCategory.fromValue(value)
    }

    @TypeConverter
    fun enumToDatabase(value: PuzzleCategory?): Int? {
        return value?.value
    }
}
