package com.simferopol.app.ui.routes.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.app.ui.map.BaseMapVm
import com.simferopol.app.ui.map.base.IMapView
import com.simferopol.app.ui.routes.RouteMapFragmentDirections
import com.yandex.mapkit.geometry.Point

class RouteMapVm(view: IMapView, routeVm: Route) : BaseMapVm(view) {

    val route = routeVm
    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoObjects
    val start = Point(routeVm.startLat, routeVm.startLon)
    val finish = Point(routeVm.finishLat, routeVm.finishLon)

    val currentObject = MutableLiveData<GeoObject>()

    fun onMonumentClick(view: View, monument: GeoObject) {
        val action = RouteMapFragmentDirections.actionNavRouteMapToNavMonument(monument)
        view.findNavController().navigate(action)
    }

    fun onRouteClick(view: View) {
        val action = RouteMapFragmentDirections.actionNavRouteMapToNavRoute(route)
        view.findNavController().navigate(action)
    }
}