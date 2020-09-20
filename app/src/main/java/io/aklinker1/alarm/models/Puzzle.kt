package io.aklinker1.alarm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "puzzles")
data class Puzzle constructor(
    @ColumnInfo(name = "alarm_id", index = true) var alarmId: Long,
    @ColumnInfo var type: PuzzleType,
    @ColumnInfo var category: PuzzleCategory,

    @ColumnInfo(name = "grid_size") var gridSize: Int? = null,
    @ColumnInfo(name = "grid_count") var gridCount: Int? = null,

    @ColumnInfo(name = "shape_count") var shapeCount: Int? = null,
    @ColumnInfo(name = "shape_pool_size") var shapePoolSize: Int? = null,
    @ColumnInfo(name = "shape_complexity") var shapeComplexity: ShapeComplexity? = null,

    @ColumnInfo @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "created_at") var createdAt: Calendar = Calendar.getInstance()
) : Serializable {
    companion object {
        fun MemoryGridHighlights(alarmId: Long, gridSize: Int = 5, gridHighlightCount: Int = 5): Puzzle {
            return Puzzle(
                alarmId,
                PuzzleType.GridHighlights,
                PuzzleCategory.Memory,
                gridSize = gridSize,
                gridCount = gridHighlightCount
            )
        }

        fun MemoryGridSequence(alarmId: Long, gridSize: Int = 5, maxNumber: Int = 5): Puzzle {
            return Puzzle(
                alarmId,
                PuzzleType.GridSequence,
                PuzzleCategory.Memory,
                gridSize = gridSize,
                gridCount = maxNumber
            )
        }

        fun MemoryShapeSequence(alarmId: Long, shapeCount: Int = 4, shapePoolSize: Int = 4, shapeComplexity: ShapeComplexity = ShapeComplexity.Easy): Puzzle {
            return Puzzle(
                alarmId,
                PuzzleType.ShapeSequence,
                PuzzleCategory.Memory,
                shapeCount = shapeCount,
                shapePoolSize = shapePoolSize,
                shapeComplexity = shapeComplexity
            )
        }
    }

    fun title(): String {
        return when (type) {
            PuzzleType.GridHighlights -> "Highlighted Squares"
            PuzzleType.GridSequence -> "Grid Sequence"
            PuzzleType.ShapeSequence -> "Shape Sequence"
        }
    }

    fun subtitle(): String {
        val categoryString = when (category) {
            PuzzleCategory.Memory -> "Memory"
        }
        val infoString =  when (type) {
            PuzzleType.GridHighlights -> "${gridSize}x$gridSize, $gridCount squares"
            PuzzleType.GridSequence -> "${gridSize}x$gridSize, Count to $gridCount"
            PuzzleType.ShapeSequence -> "Shape Sequence"
        }
        return "$infoString  •  $categoryString"
    }
}