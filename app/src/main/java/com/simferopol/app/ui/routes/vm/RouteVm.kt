package com.simferopol.app.ui.routes.vm

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Route
import com.simferopol.app.ui.routes.RoutesFragmentDirections

class RouteVm(routeVm: Route) : ViewModel() {
    val route = routeVm
    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoObjects

    fun onTouch(view: View, motionEvent: MotionEvent):Boolean {
        when (motionEvent.action){
            MotionEvent.ACTION_DOWN -> view.alpha = 0.9f
            MotionEvent.ACTION_CANCEL -> view.alpha = 0.3f
            MotionEvent.ACTION_UP -> {
                view.alpha = 0.3f
                onClick(view)
            }
        }
        return  true
    }

    private fun onClick(view: View){
        val action = RoutesFragmentDirections.actionNavRoutesToNavRoute(route)
        view.findNavController().navigate(action)
    }

    fun routeClick(view: ImageView, name: String?) {
        view.setOnClickListener {
            Log.e("routesBtn", name)
        }
    }
}