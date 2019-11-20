package com.simferopol.app.ui.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App
import com.simferopol.app.ui.routes.routeGeoObjects.vm.GeoObjectVm
import com.simferopol.app.utils.models.ViewState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

val simfer = LatLng(44.949684, 34.102521)
val simferBounds = LatLngBounds(LatLng(44.888679, 34.010726), LatLng(45.009175, 34.191087))

class MapVM : ViewModel(), OnMapReadyCallback {

    var currentZoom = 14f
    val currentObject = MutableLiveData<GeoObject>()
    val listOfGeoObjects = MutableLiveData(ArrayList<GeoObject>())


    fun onSelectRouteClick() {
        Log.e("selectRoute", "click")// todo navigate to select route
    }

    fun onArClick() {
        Log.e("ar", "click")// todo navigate to AR Screen
    }

    fun onLocateClick() {
        Log.e("locate", "click")// todo locate
    }

    fun onZoomInClick() {
        Log.e("zoomIn", "click")// todo zoomIn
        currentZoom += 1f
        map?.animateCamera(CameraUpdateFactory.zoomTo(currentZoom), 400, null)
    }

    fun onZoomOutClick() {
        Log.e("zoomOut", "click")// todo zoomOut
        currentZoom -= 1f
        map?.animateCamera(CameraUpdateFactory.zoomTo(currentZoom), 400, null)
    }

    var map: GoogleMap? = null

    val viewState = MutableLiveData(ViewState.LOADING)

    private val routeManager by App.kodein.instance<ApiManager>()


    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects(null)
            if (result.success) {
                listOfGeoObjects.postValue(ArrayList(result.data?.map { it } ?: ArrayList()))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewState.postValue(ViewState.CONTENT)

        initGoogleMapAttrs()

        initMapPosition()

//        list.value?.forEach { map!!
//            .addMarker(MarkerOptions())
//            .position(LatLng(it.lat, it.lon)) }
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
        map?.setLatLngBoundsForCameraTarget(simferBounds)
        map?.moveCamera(CameraUpdateFactory.newLatLng(simfer))
        map?.animateCamera(CameraUpdateFactory.zoomTo(currentZoom), 400, null)
    }
}