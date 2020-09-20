package io.aklinker1.alarm.adapters.view_holders

interface PuzzleListItemClickListener {
    fun onClickItem(puzzleId: Long)
    fun onClickDeleteItem(puzzleId: Long)
}