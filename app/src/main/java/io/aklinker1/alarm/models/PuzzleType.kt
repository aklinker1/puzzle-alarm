package io.aklinker1.alarm.models

import androidx.room.TypeConverter

enum class PuzzleType {
    GridHighlights,
    GridSequence,
    ShapeSequence;

    companion object {
        private const val GRID_HIGHLIGHTS = 0
        private const val GRID_SEQUENCE = 1
        private const val SHAPE_SEQUENCE = 2
    }

    @TypeConverter
    fun databaseToEnum(value: Int?): PuzzleType? {
        return when(value) {
            GRID_HIGHLIGHTS -> GridHighlights
            GRID_SEQUENCE -> GridSequence
            SHAPE_SEQUENCE -> ShapeSequence
            else -> null
        }
    }

    @TypeConverter
    fun enumToDatabase(value: PuzzleType?): Int? {
        return when(value) {
            GridHighlights -> GRID_HIGHLIGHTS
            GridSequence -> GRID_SEQUENCE
            ShapeSequence -> SHAPE_SEQUENCE
            else -> null
        }
    }
}