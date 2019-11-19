package com.simferopol.app.ui.routes.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.simferopol.api.models.Route
import com.simferopol.app.ui.routes.RoutesFragmentDirections
import com.simferopol.app.utils.models.ViewState

class RouteMapVm(routeVm: Route) : ViewModel(), OnMapReadyCallback {

    val route = routeVm
    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoObjects

    var map: GoogleMap? = null
    val viewState = MutableLiveData(ViewState.LOADING)

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewState.postValue(ViewState.CONTENT)

        initGoogleMapAttrs()

        initMapPosition()
    }

    private fun initGoogleMapAttrs() {
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL

        map?.uiSettings?.isCompassEnabled = false
        map?.uiSettings?.isZoomControlsEnabled = false
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isTiltGesturesEnabled = false
        map?.uiSettings?.isMapToolbarEnabled = false
        map?.uiSettings?.isRotateGesturesEnabled = false
        map?.uiSettings?.isScrollGesturesEnabled = true

        map?.isTrafficEnabled = false
        map?.isBuildingsEnabled = false

        map?.setMinZoomPreference(10f)
    }

    private fun initMapPosition() {
        map?.setLatLngBoundsForCameraTarget(com.simferopol.app.ui.map.simferBounds)
        map?.moveCamera(CameraUpdateFactory.newLatLng(com.simferopol.app.ui.map.simfer))
        map?.animateCamera(CameraUpdateFactory.zoomTo(14f), 400, null)
    }

    fun onClick(view: View) {
        val action = RoutesFragmentDirections.actionNavRoutesToNavRoute(route)
        view.findNavController().navigate(action)
    }
}