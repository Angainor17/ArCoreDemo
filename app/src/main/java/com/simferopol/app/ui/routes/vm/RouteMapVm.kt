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

class RouteMapVm(routeVm: Route) : ViewModel() {

    val route = routeVm
    val routeId = routeVm.id
    val imageUrl = routeVm.preview
    val name = routeVm.name
    val distance = routeVm.distance
    val time = routeVm.time
    val geoObjects = routeVm.geoObjects

}