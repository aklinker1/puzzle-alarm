package io.aklinker1.alarm.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.aklinker1.alarm.adapters.view_holders.*
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.models.Puzzle
import io.aklinker1.alarm.utils.CustomViewHolder

class EditAlarmAdapter(
    private val puzzleClickHandler: PuzzleListItemClickListener,
    private val weekdaySelectorClickListener: WeekdaySelectorListItemClickListener,
    private val addPuzzleClickListener: AddPuzzleListItemClickListener
) :
    ListAdapter<Any, CustomViewHolder<Any>>(DIFF_CALLBACK) {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_WEEKLY_SELECTOR = 1
        const val VIEW_TYPE_PUZZLE = 2
        const val VIEW_TYPE_ADD_PUZZLE = 3

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is String && newItem is String) return oldItem == newItem
                if (oldItem is Boolean && newItem is Boolean) return oldItem == newItem
                if (oldItem is Puzzle && newItem is Puzzle) return oldItem.id == newItem.id
                if (oldItem is Alarm && newItem is Alarm) return oldItem.id == newItem.id
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is String && newItem is String) return (oldItem as String) == (newItem as String)
                if (oldItem is Boolean && newItem is Boolean) return (oldItem as Boolean) == (newItem as Boolean)
                if (oldItem is Puzzle && newItem is Puzzle) return (oldItem as Puzzle) == (newItem as Puzzle)
                if (oldItem is Alarm && newItem is Alarm) {
                    return oldItem.repeatSunday == newItem.repeatSunday
                            && oldItem.repeatMonday == newItem.repeatMonday
                            && oldItem.repeatTuesday == newItem.repeatTuesday
                            && oldItem.repeatWednesday == newItem.repeatWednesday
                            && oldItem.repeatThursday == newItem.repeatThursday
                            && oldItem.repeatFriday == newItem.repeatFriday
                            && oldItem.repeatSaturday == newItem.repeatSaturday
                }
                return false
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is String -> VIEW_TYPE_HEADER
            is Puzzle -> VIEW_TYPE_PUZZLE
            is Alarm -> VIEW_TYPE_WEEKLY_SELECTOR
            is Boolean -> VIEW_TYPE_ADD_PUZZLE
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder<Any>, position: Int) {
        holder.bind(getItem(position))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<Any> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> SectionHeaderListItemViewHolder(parent) as CustomViewHolder<Any>
            VIEW_TYPE_WEEKLY_SELECTOR -> WeekdaySelectorListItemViewHolder(
                weekdaySelectorClickListener, parent
            ) as CustomViewHolder<Any>
            VIEW_TYPE_PUZZLE -> PuzzleListItemViewHolder(
                puzzleClickHandler, parent
            ) as CustomViewHolder<Any>
            VIEW_TYPE_ADD_PUZZLE -> AddPuzzleListItemViewHolder(
                addPuzzleClickListener,
                parent
            ) as CustomViewHolder<Any>
            else -> throw Error("Unknown view type = $viewType")
        }
    }

    fun submitList(alarm: Alarm?, puzzles: List<Puzzle>?) {
        val items = ArrayList<Any>()
        if (alarm != null) {
            items.add("Repeats")
            items.add(alarm)
        }
        items.add("Puzzles")
        if (puzzles != null) {
            items.addAll(puzzles)
        }
        items.add(false)

        submitList(items)
    }
}
