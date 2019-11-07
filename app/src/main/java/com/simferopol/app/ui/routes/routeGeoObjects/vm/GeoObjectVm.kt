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


    fun onItemTouch(view: View, motionEvent: MotionEvent):Boolean
    {
        when (motionEvent.action){
            MotionEvent.ACTION_DOWN -> { }
            MotionEvent.ACTION_UP -> {
                onItemClick(view)
            }
        }
        return  true
    }

    private fun onItemClick(view: View){
    Log.e("object", name)//todo navigation to geoObject Screen
    }
}