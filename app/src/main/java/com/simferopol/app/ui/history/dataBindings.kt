package com.simferopol.app.ui.history

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.simferopol.app.R
import com.simferopol.app.ui.history.vm.HistoryVm
import com.simferopol.app.utils.ui.FixedListAdapter
import com.simferopol.app.utils.ui.StoriesPagerAdapter

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

@BindingAdapter("app:initStoriesPager")
fun initStoriesPager(view: ViewPager, list: ArrayList<HistoryVm>) {
    val adapter = object : StoriesPagerAdapter() {}
    adapter.setItems(list)
    view.adapter = adapter
}

@BindingAdapter("app:initPagerPosition")
fun initPagerPosition(view: ViewPager, id: Int) {
    view.currentItem = id
}



