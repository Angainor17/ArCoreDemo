package com.simferopol.app.ui.routes.vm

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.Route

class RouteVm(routeVm: Route) : ViewModel() {
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance.toString()
    val time = routeVm.time.toString()

    fun onTouch(view: View, motionEvent: MotionEvent):Boolean {
        when (motionEvent.action){
            MotionEvent.ACTION_DOWN -> { view.alpha = 0.9f }
            MotionEvent.ACTION_UP -> {
                view.alpha = 0.3f
                onClick()
            }
        }
        return  true
    }

    private fun onClick(){
        Log.e("item", name)//todo on item click
    }
}