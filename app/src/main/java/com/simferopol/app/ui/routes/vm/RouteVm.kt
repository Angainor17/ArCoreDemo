package com.simferopol.app.ui.routes.vm

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Route
import com.simferopol.app.ui.routes.RoutesFragmentDirections

class RouteVm(routeVm: Route) : ViewModel() {

    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoobjects

    fun onTouch(view: View, motionEvent: MotionEvent):Boolean {
        when (motionEvent.action){
            MotionEvent.ACTION_DOWN -> { view.alpha = 0.9f }
            MotionEvent.ACTION_UP -> {
                view.alpha = 0.3f
                onClick(view)
            }
        }
        return  true
    }

    private fun onClick(view: View){
        val action = RoutesFragmentDirections.actionNavRoutesToNavRoute(routeId,name,imageUrl,distance,time)
        view.findNavController().navigate(action)
    }
}