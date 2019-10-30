package com.simferopol.app.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.simferopol.app.utils.models.ViewState

val sydney = LatLng((-34).toDouble(), 151.toDouble())
val RUSSIA_RECT = LatLngBounds(LatLng(41.191055, 20.223023), LatLng(80.213079, 150.141353))

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

        map?.setMaxZoomPreference(5f)
        map?.setMinZoomPreference(2f)
    }

    private fun initMapPosition() {
        map?.setLatLngBoundsForCameraTarget(RUSSIA_RECT)
        map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}