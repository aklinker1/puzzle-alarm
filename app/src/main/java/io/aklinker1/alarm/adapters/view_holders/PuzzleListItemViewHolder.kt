package io.aklinker1.alarm.adapters.view_holders

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.models.Puzzle
import io.aklinker1.alarm.utils.CustomViewHolder

class PuzzleListItemViewHolder(
    private val clickHandler: PuzzleListItemClickListener, parent: ViewGroup
) : CustomViewHolder<Puzzle>(parent, R.layout.list_item_puzzle) {

    private val click = itemView.findViewById<View>(R.id.click)
    private val icon = itemView.findViewById<ImageView>(R.id.icon)
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val subtitle = itemView.findViewById<TextView>(R.id.subtitle)
    private val delete = itemView.findViewById<ImageView>(R.id.delete)

    override fun bind(data: Puzzle) {
        title.text = data.title()
        subtitle.text = data.subtitle()
        icon.setImageResource(R.drawable.puzzle_grid_highlight)

        delete.setOnClickListener {
            clickHandler.onClickDeleteItem(data.id)
        }
        click.setOnClickListener {
            clickHandler.onClickItem(data.id)
        }
    }
}