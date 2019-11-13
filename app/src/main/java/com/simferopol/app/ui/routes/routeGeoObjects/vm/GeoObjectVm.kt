package com.simferopol.app.ui.routes.routeGeoObjects.vm

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.GeoObject

class GeoObjectVm(geoObject: GeoObject) : ViewModel() {

    val name = geoObject.name
    val address = geoObject.address
    val about = geoObject.about

    var index = 0
    var totalItems = 0

    fun isFirst(): Boolean = index == 0

    fun isLast(): Boolean = index == totalItems - 1

     fun onItemClick(view: View) {
        Log.e("object", name)//todo navigation to geoObject Screen
    }
}