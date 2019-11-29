package com.simferopol.app.utils.dataBindings

import androidx.databinding.BindingAdapter
import com.simferopol.api.models.GeoObject
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@BindingAdapter("app:zoom")
fun zoom(view: MapView, zoom: Float) {
    if (zoom == 0f) return
    view.map.move(CameraPosition(view.map.cameraPosition.target, zoom, 0.0f, 0.0f))
}

@BindingAdapter("app:geoObjects")
fun geoObjects(mapView: MapView, list: ArrayList<GeoObject>) {

}

