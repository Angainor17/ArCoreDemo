package com.simferopol.app.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.simferopol.app.utils.models.ViewState

val simferopol = LatLng(44.949684, 34.102521)
val SIMFER_RECT = LatLngBounds(LatLng(44.888679, 34.010726), LatLng(45.009175, 34.191087))

class MapVM : ViewModel(), OnMapReadyCallback {

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
        map?.setLatLngBoundsForCameraTarget(SIMFER_RECT)
        map?.moveCamera(CameraUpdateFactory.newLatLng(simferopol))
        map?.animateCamera(CameraUpdateFactory.zoomTo(14f), 400, null)
    }
}