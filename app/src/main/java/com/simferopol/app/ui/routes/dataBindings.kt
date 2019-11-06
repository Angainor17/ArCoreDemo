package com.simferopol.app.ui.routes

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.routes.vm.RouteVm
import com.simferopol.app.utils.ui.FixedListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.route_list_item.view.*

@BindingAdapter("app:initRouteList")
fun initRouteList(view: RecyclerView, list: ArrayList<RouteVm>) {
    val adapter = object : FixedListAdapter<RouteVm>(R.layout.route_list_item) {
        override fun onBindViewHolder(holder: VH<RouteVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
            holder.itemView.previewImage.setOnTouchListener { view, motionEvent ->
                item.onTouch(view, motionEvent)
            }
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?){
    if(!url.isNullOrEmpty()){
        Picasso.get()
            .load(url)
            .into(view)
    }
}

