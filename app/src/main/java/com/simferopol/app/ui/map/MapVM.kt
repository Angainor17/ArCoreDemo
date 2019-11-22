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
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class MapVM : ViewModel(){

    lateinit var mapview: MapView
    var currentZoom = 14f
    val currentObject = MutableLiveData<GeoObject>()
    val listOfGeoObjects = MutableLiveData(ArrayList<GeoObject>())
    val viewState = MutableLiveData(ViewState.LOADING)


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
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target,currentZoom,0.0f, 0.0f))

    }

    fun onZoomOutClick() {
        Log.e("zoomOut", "click")// todo zoomOut
        currentZoom -= 1f
        mapview.map.move(CameraPosition(mapview.map.cameraPosition.target,currentZoom,0.0f, 0.0f))
    }

}