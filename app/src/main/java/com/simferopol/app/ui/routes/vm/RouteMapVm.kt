package com.simferopol.app.ui.routes.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.app.ui.routes.RouteMapFragmentDirections
import com.simferopol.app.utils.models.ViewState
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class RouteMapVm(routeVm: Route) : ViewModel() {

    val route = routeVm
    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoObjects

    lateinit var mapview: MapView
    var currentZoom = 14f
    val currentObject = MutableLiveData<GeoObject>()
    val viewState = MutableLiveData(ViewState.LOADING)

    fun onMonumentClick(view: View, monument: GeoObject) {
        val action = RouteMapFragmentDirections.actionNavRouteMapToNavMonument(monument)
        view.findNavController().navigate(action)
    }

    fun onRouteClick(view: View) {
        val action = RouteMapFragmentDirections.actionNavRouteMapToNavRoute(route)
        view.findNavController().navigate(action)
    }

    fun onZoomInClick() {
        currentZoom += 1f
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target, currentZoom, 0.0f, 0.0f))
    }

    fun onZoomOutClick() {
        currentZoom -= 1f
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target, currentZoom, 0.0f, 0.0f))
    }
}