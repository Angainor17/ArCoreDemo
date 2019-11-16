package com.simferopol.app.ui.services

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.services.vm.ServiceVm
import com.simferopol.app.utils.ui.FixedListAdapter

@BindingAdapter("app:initServicesList")
fun initServicesList(view: RecyclerView, list: ArrayList<ServiceVm>) {
    val adapter = object : FixedListAdapter<ServiceVm>(R.layout.service_list_item) {
        override fun onBindViewHolder(holder: VH<ServiceVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
    view.layoutManager = GridLayoutManager(view.context,2)
}


