package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class ShapeComplexity(val value: Int) {
    Easy(0),
    Medium(1),
    Hard(2),
    Impossible(3);

    companion object {
        fun fromValue(value: Int?): ShapeComplexity? {
            return when (value) {
                0 -> Easy
                1 -> Medium
                2 -> Hard
                3 -> Impossible
                else -> null
            }
        }
    }
}

class ShapeComplexityConverter {
    @TypeConverter
    fun databaseToEnum(value: Int?): ShapeComplexity? {
        return ShapeComplexity.fromValue(value)
    }

    @TypeConverter
    fun enumToDatabase(value: ShapeComplexity?): Int? {
        return value?.value
    }
}