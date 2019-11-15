package com.simferopol.app.ui.history

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.simferopol.api.models.HistoricalEvent
import com.simferopol.app.R
import com.simferopol.app.ui.history.vm.EventVm
import com.simferopol.app.ui.history.vm.HistoryVm
import com.simferopol.app.utils.ui.FixedListAdapter
import com.simferopol.app.utils.ui.StoriesPagerAdapter
import kotlinx.android.synthetic.main.event_list_item.view.*

@BindingAdapter("app:initStoriesList")
fun initStoriesList(view: RecyclerView, list: ArrayList<HistoryVm>) {
    val adapter = object : FixedListAdapter<HistoryVm>(R.layout.story_list_item) {
        override fun onBindViewHolder(holder: VH<HistoryVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
}

@BindingAdapter("app:initStoriesPager", "app:initPagerPosition")
fun initStoriesPager(view: ViewPager, list: ArrayList<HistoryVm>, id: Int) {
    val adapter = object : StoriesPagerAdapter() {}
    adapter.setItems(list)
    view.adapter = adapter
    view.currentItem = id
}

@BindingAdapter("app:initEventList")
fun initEventList(view: RecyclerView, list: ArrayList<EventVm>) {
    val adapter = object : FixedListAdapter<EventVm>(R.layout.event_list_item) {
        override fun onBindViewHolder(holder: VH<EventVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            if (item.title.isNullOrEmpty()) holder.itemView.title.visibility = View.GONE
            if (item.subtitle.isNullOrEmpty()) holder.itemView.subtitle.visibility = View.GONE
            if (item.incut.isNullOrEmpty()) holder.itemView.incut.visibility = View.GONE
            if (item.content.isNullOrEmpty()) holder.itemView.content.visibility = View.GONE
            holder.bind(item)
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
}


