package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class ShapeComplexity {
    Easy,
    Medium,
    Hard,
    Impossible;

    companion object {
        private const val EASY = 0
        private const val MEDIUM = 1
        private const val HARD = 2
        private const val IMPOSSIBLE = 3
    }

    @TypeConverter
    fun databaseToEnum(value: Int?): ShapeComplexity? {
        return when(value) {
            EASY -> Easy
            MEDIUM -> Medium
            HARD -> Hard
            IMPOSSIBLE -> Impossible
            else -> null
        }
    }

    @TypeConverter
    fun enumToDatabase(value: ShapeComplexity?): Int? {
        return when(value) {
            Easy -> EASY
            Medium -> MEDIUM
            Hard -> HARD
            Impossible -> IMPOSSIBLE
            else -> null
        }
    }
}
