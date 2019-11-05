package com.simferopol.app.ui.routes

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.routes.vm.RouteVm
import com.simferopol.app.utils.ui.FixedListAdapter

@BindingAdapter("app:initRouteList")
fun initRouteList(view: RecyclerView, list: ArrayList<RouteVm>) {
    val adapter = object : FixedListAdapter<RouteVm>(R.layout.route_list_item) {}
    adapter.setItems(list)

    view.adapter = adapter
}