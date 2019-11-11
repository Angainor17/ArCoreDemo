package com.simferopol.app.ui.routes

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.R
import com.simferopol.app.ui.routes.routeGeoObjects.vm.GeoObjectVm
import com.simferopol.app.ui.routes.vm.RouteVm
import com.simferopol.app.utils.ui.FixedListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.geoobject_list_item.view.*
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

@BindingAdapter("app:initGeoObjectsList")
fun initGeoObjectsList(view: RecyclerView, list: ArrayList<GeoObjectVm>) {
    val adapter = object : FixedListAdapter<GeoObjectVm>(R.layout.geoobject_list_item) {
        override fun onBindViewHolder(holder: VH<GeoObjectVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            item.index = position
            item.totalItems = itemCount
            holder.bind(item)
            holder.itemView.geoObjectView.setOnTouchListener { view, motionEvent ->
                item.onItemTouch(view, motionEvent)
            }
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
}

@BindingAdapter("app:routeClick")
fun routeClick(view: ImageView, name: String?) {
    view.setOnClickListener {
        Log.e("routesBtn", name)
    }
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get()
            .load(url)
            .into(view)
    }
}

@BindingAdapter("app:hourText")
fun hourText(view: TextView, time: Float) {
    val plural = if (time == 1f) " Час"
    else " Часа"
    val text = String(Character.toChars(0x2248)) + time.toString() + plural
    view.text = text
}

