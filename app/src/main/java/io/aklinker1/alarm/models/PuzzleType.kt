package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class PuzzleType(val value: Int) {
    GridHighlights(0),
    GridSequence(1),
    ShapeSequence(2);

    companion object {
        fun fromValue(value: Int?): PuzzleType? {
            return when(value) {
                0 -> GridHighlights
                1 -> GridSequence
                2 -> ShapeSequence
                else -> null
            }
        }
    }

}

class PuzzleTypeConverter {
    @TypeConverter
    fun databaseToEnum(value: Int?): PuzzleType? {
        return PuzzleType.fromValue(value)
    }

    @TypeConverter
    fun enumToDatabase(value: PuzzleType?): Int? {
        return value?.value
    }
}