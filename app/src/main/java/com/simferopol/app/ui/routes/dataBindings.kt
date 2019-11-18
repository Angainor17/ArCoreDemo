package com.simferopol.app.ui.routes

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.R
import com.simferopol.app.providers.res.IResProvider
import com.simferopol.app.ui.routes.routeGeoObjects.vm.GeoObjectVm
import com.simferopol.app.ui.routes.vm.RouteVm
import com.simferopol.app.utils.ui.FixedListAdapter
import com.squareup.picasso.Picasso
import org.kodein.di.direct
import org.kodein.di.generic.instance

@BindingAdapter("app:initRouteList")
fun initRouteList(view: RecyclerView, list: ArrayList<RouteVm>) {
    val adapter = object : FixedListAdapter<RouteVm>(R.layout.route_list_item) {
        override fun onBindViewHolder(holder: VH<RouteVm>, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = list[position]
            holder.bind(item)
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
        }
    }
    adapter.setItems(list)

    view.adapter = adapter
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
    val plural = if (time == 1.0f) view.context.resources.getQuantityString(
        R.plurals.time_hours,
        1,
        time.toString()
    )
    else if (time < 5.0f) view.context.resources.getQuantityString(
        R.plurals.time_hours,
        2,
        time.toString()
    )
    else view.context.resources.getQuantityString(R.plurals.time_hours, 5, time.toString())
    val text = String(Character.toChars(0x2248)) + plural
    view.text = text
}

@BindingAdapter("app:distanceText")
fun distanceText(view: TextView, distance: Float) {
    val resProvider: IResProvider = kodein.direct.instance()
    val formattedText = "$distance " + resProvider.getString(R.string.distance_km)
    view.text = formattedText
}