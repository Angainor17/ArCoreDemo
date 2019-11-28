package com.simferopol.app.ui.routes.routeGeoObjects.vm

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.app.ui.routes.routeGeoObjects.RouteFragmentDirections

class GeoObjectVm(geoObject: GeoObject) : ViewModel() {

    val name = geoObject.name
    val address = geoObject.address
    val about = geoObject.about
    val monument = geoObject

    var index = 0
    var totalItems = 0

    fun isFirst(): Boolean = index == 0

    fun isLast(): Boolean = index == totalItems - 1

     fun onItemClick(view: View) {
         val action = RouteFragmentDirections.actionNavRouteToNavMonument(monument)
         view.findNavController().navigate(action)
    }
}