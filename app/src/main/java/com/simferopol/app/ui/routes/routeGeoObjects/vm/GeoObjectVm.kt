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

    fun onItemTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> view.alpha = 0.3f
            MotionEvent.ACTION_CANCEL -> view.alpha = 0f
            MotionEvent.ACTION_UP -> {
                view.alpha = 0f
                onItemClick(view)
            }
        }
        return true
    }

    private fun onItemClick(view: View) {
        Log.e("object", name)//todo navigation to geoObject Screen
    }
}