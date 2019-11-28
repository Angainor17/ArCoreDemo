package com.simferopol.app.ui.map

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.GeoObject
import com.simferopol.app.utils.models.ViewState
import com.yandex.mapkit.map.CameraPosition

class MapVM : ViewModel() {

    lateinit var mapview: com.yandex.mapkit.mapview.MapView
    var currentZoom = 14f
    val currentObject = MutableLiveData<GeoObject>()
    val listOfGeoObjects = MutableLiveData(ArrayList<GeoObject>())
    val viewState = MutableLiveData(ViewState.LOADING)

    fun onMonumentClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavMonument(currentObject.value!!)
        view.findNavController().navigate(action)
    }

    fun onSelectRouteClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavRoutes()
        view.findNavController().navigate(action)
    }

    fun onArClick() {
        Log.e("ar", "click")// todo navigate to AR Screen
    }

    fun onLocateClick() {
        Log.e("locate", "click")// todo locate
    }

    fun onZoomInClick() {
        Log.e("zoomIn", "click")
        currentZoom += 1f
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target, currentZoom, 0.0f, 0.0f))
    }

    fun onZoomOutClick() {
        Log.e("zoomOut", "click")
        currentZoom -= 1f
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target, currentZoom, 0.0f, 0.0f))
    }
}