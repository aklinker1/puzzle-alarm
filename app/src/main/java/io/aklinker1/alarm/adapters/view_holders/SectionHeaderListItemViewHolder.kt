package io.aklinker1.alarm.adapters.view_holders

import android.view.ViewGroup
import android.widget.TextView
import io.aklinker1.alarm.R
import io.aklinker1.alarm.utils.CustomViewHolder

class SectionHeaderListItemViewHolder(parent: ViewGroup) :
    CustomViewHolder<String>(parent, R.layout.list_item_section_header) {

    override fun bind(text: String) {
        (itemView as TextView).text = text
    }
}
