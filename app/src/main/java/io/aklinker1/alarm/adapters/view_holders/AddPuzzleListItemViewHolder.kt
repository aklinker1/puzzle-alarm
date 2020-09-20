package io.aklinker1.alarm.adapters.view_holders

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import io.aklinker1.alarm.R
import io.aklinker1.alarm.utils.CustomViewHolder

class AddPuzzleListItemViewHolder(private val clickListener: AddPuzzleListItemClickListener, parent: ViewGroup) :
    CustomViewHolder<Boolean>(parent, R.layout.list_item_add_puzzles) {

    val click = itemView.findViewById<Button>(R.id.add_puzzle)
    override fun bind(data: Boolean) {
        click.setOnClickListener {
            clickListener.onClickAddPuzzle()
        }
    }
}