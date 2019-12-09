package com.simferopol.app.ui.routes.vm

import android.view.View
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
    val kids = routeVm.kids
    val disabled = routeVm.disabled

    fun onClick(view: View) {
        val action = RoutesFragmentDirections.actionNavRoutesToNavRoute(route)
        view.findNavController().navigate(action)
    }
}