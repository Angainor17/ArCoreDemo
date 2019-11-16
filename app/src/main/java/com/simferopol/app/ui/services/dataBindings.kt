package com.simferopol.app.ui.services

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.services.vm.CategoryVm
import com.simferopol.app.ui.services.vm.ServiceVm
import com.simferopol.app.utils.ui.FixedListAdapter

@BindingAdapter("app:initCategoryList")
fun initCategoryList(view: RecyclerView, list: ArrayList<CategoryVm>) {
    val adapter = object : FixedListAdapter<CategoryVm>(R.layout.category_list_item) {
        override fun onBindViewHolder(holder: VH<CategoryVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
    view.layoutManager = GridLayoutManager(view.context, 2)
}

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
    view.layoutManager = LinearLayoutManager(view.context)
}

@BindingAdapter("app:textViewVisibility")
fun textViewVisibility(view: TextView, text: String?) {
    if (text.isNullOrEmpty()) view.visibility = View.GONE
}


