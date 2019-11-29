package com.simferopol.app.ui.map

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.simferopol.app.ui.map.base.IMapView
import com.simferopol.app.utils.ui.CustomUserLocationObjectListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import kotlinx.android.synthetic.main.map_view.*

abstract class BaseMapFragment : Fragment(), IMapView {

    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var userLocationObjectListener: CustomUserLocationObjectListener

    abstract fun createVm(): BaseMapVm

    override fun findActivity(): Activity = activity!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        userLocationObjectListener = CustomUserLocationObjectListener(context!!)
    }

    override fun findMapView(): MapView = mapView

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLocationObjectListener = CustomUserLocationObjectListener(context!!)
        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = false
        userLocationLayer.isHeadingEnabled = false
        userLocationLayer.setObjectListener(userLocationObjectListener)
    }

    override fun showUserLocation() {
        MapKitFactory.getInstance().createLocationManager().subscribeForLocationUpdates(
            0.0,
            600,
            1.0,
            false,
            FilteringMode.OFF,
            object : LocationListener {
                override fun onLocationStatusUpdated(p0: LocationStatus) = Unit

                override fun onLocationUpdated(location: Location) {
                    userLocationLayer.isVisible = true
                    mapView.map.move(
                        CameraPosition(location.position, createVm().zoom, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 1f),
                        null
                    )
                }
            })
    }
}