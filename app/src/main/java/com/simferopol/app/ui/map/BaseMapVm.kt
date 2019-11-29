package com.simferopol.app.ui.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.app.MY_PERMISSIONS_REQUEST_FINE_LOCATION
import com.simferopol.app.ui.map.base.IMapView
import com.simferopol.app.utils.models.ViewState

open class BaseMapVm(val view: IMapView) : ViewModel() {

    val viewState = MutableLiveData(ViewState.LOADING)

    var zoom = 14f
    val currentZoom = MutableLiveData<Float>()

    fun onZoomInClick() {
        zoom += 1f
        currentZoom.postValue(zoom)
    }

    fun onZoomOutClick() {
        zoom -= 1f
        currentZoom.postValue(zoom)
    }

    fun onGeoLocationClick() {
        val activity = view.findActivity()
        if (checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)) {
                requestPermissions(
                    activity,
                    arrayOf(ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION
                )
            }
        } else {
            view.showUserLocation()
        }
    }
}