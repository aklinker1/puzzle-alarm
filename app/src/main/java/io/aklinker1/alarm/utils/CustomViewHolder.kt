package io.aklinker1.alarm.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class CustomViewHolder<in T>(parent: ViewGroup, @LayoutRes layout: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layout, parent, false)) {
    abstract fun bind(t: T)
}
