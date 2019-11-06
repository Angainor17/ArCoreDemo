package com.simferopol.app.ui.routes.vm

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Route
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App
import com.simferopol.app.R
import org.kodein.di.generic.instance

class RouteVm(routeVm: Route) : ViewModel() {

    private val routeManager by App.kodein.instance<RouteManager>()

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
        view.findNavController().navigate(R.id.nav_route)
    }
}