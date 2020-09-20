package io.aklinker1.alarm.adapters.view_holders

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import io.aklinker1.alarm.R
import io.aklinker1.alarm.models.Alarm
import io.aklinker1.alarm.utils.CustomViewHolder

class WeekdaySelectorListItemViewHolder(
    private val clickHandler: WeekdaySelectorListItemClickListener, parent: ViewGroup
) :
    CustomViewHolder<Alarm>(parent, R.layout.list_item_weekday_selector) {

    private val sunday: FrameLayout = itemView.findViewById(R.id.sunday)
    private val monday: FrameLayout = itemView.findViewById(R.id.monday)
    private val tuesday: FrameLayout = itemView.findViewById(R.id.tuesday)
    private val wednesday: FrameLayout = itemView.findViewById(R.id.wednesday)
    private val thursday: FrameLayout = itemView.findViewById(R.id.thursday)
    private val friday: FrameLayout = itemView.findViewById(R.id.friday)
    private val saturday: FrameLayout = itemView.findViewById(R.id.saturday)

    override fun bind(data: Alarm) {
        sunday.isSelected = data.repeatSunday
        monday.isSelected = data.repeatMonday
        tuesday.isSelected = data.repeatTuesday
        wednesday.isSelected = data.repeatWednesday
        thursday.isSelected = data.repeatThursday
        friday.isSelected = data.repeatFriday
        saturday.isSelected = data.repeatSaturday

        sunday.setOnClickListener {
            clickHandler.onClickDay(0, !sunday.isSelected)
        }
        monday.setOnClickListener {
            clickHandler.onClickDay(1, !monday.isSelected)
        }
        tuesday.setOnClickListener {
            clickHandler.onClickDay(2, !tuesday.isSelected)
        }
        wednesday.setOnClickListener {
            clickHandler.onClickDay(3, !wednesday.isSelected)
        }
        thursday.setOnClickListener {
            clickHandler.onClickDay(4, !thursday.isSelected)
        }
        friday.setOnClickListener {
            clickHandler.onClickDay(5, !friday.isSelected)
        }
        saturday.setOnClickListener {
            clickHandler.onClickDay(6, !saturday.isSelected)
        }
    }
}