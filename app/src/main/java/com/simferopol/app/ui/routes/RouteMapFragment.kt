package com.simferopol.app.ui.routes

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.api.models.GeoObject
import com.simferopol.app.MY_PERMISSIONS_REQUEST_FINE_LOCATION
import com.simferopol.app.databinding.FragmentRouteMapBinding
import com.simferopol.app.ui.map.simfer
import com.simferopol.app.ui.routes.vm.RouteMapVm
import com.simferopol.app.utils.models.ViewState
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.runtime.Error
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.simferopol.app.utils.ui.CustomInputListener
import com.simferopol.app.utils.ui.CustomUserLocationObjectListener
import com.simferopol.app.utils.ui.CustomVisitor
import com.simferopol.app.utils.ui.YandexMapUtils
import com.yandex.mapkit.*
import com.yandex.mapkit.location.*
import com.yandex.mapkit.user_location.UserLocationLayer

class RouteMapFragment : Fragment(), Session.RouteListener {

    lateinit var routeMapVM: RouteMapVm
    private val listener = YandexMapObjectTapListener()
    lateinit var inputListener: CustomInputListener
    lateinit var visitor: CustomVisitor
    lateinit var mapview: MapView
    lateinit var binding: FragmentRouteMapBinding
    lateinit var mtRouter: PedestrianRouter
    lateinit var userLocationLayer: UserLocationLayer
    private lateinit var userLocationObjectListener: CustomUserLocationObjectListener
    private lateinit var locationManager: LocationManager
    lateinit var mapKit: MapKit


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: RouteMapFragmentArgs by navArgs()
        routeMapVM = RouteMapVm(args.route)
        binding = FragmentRouteMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = routeMapVM
        mapview = binding.mapview as MapView
        routeMapVM.mapview = mapview
        binding.footerContainer.visibility = View.VISIBLE
        visitor = CustomVisitor(context!!)
        inputListener = CustomInputListener(binding.mapview)

        val points = ArrayList<RequestPoint>()
        TransportFactory.initialize(context)
        routeMapVM.geoObjects?.forEach {
            if (it.lon != null) {
                points.add(RequestPoint(Point(it.lat!!, it.lon!!), RequestPointType.WAYPOINT, null))
            }
        }
        if (points.size > 1) {
            mtRouter = TransportFactory.getInstance().createPedestrianRouter()
            mtRouter.requestRoutes(points, TimeOptions(), this)
            mapview.getMap().move(
                CameraPosition(points.first().point, routeMapVM.currentZoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
        } else mapview.getMap().move(
            CameraPosition(simfer, routeMapVM.currentZoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        mapKit = MapKitFactory.getInstance()
        binding.geoLocation.setOnClickListener {
            getLocation()
        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        if (!routeMapVM.geoObjects.isNullOrEmpty())
            YandexMapUtils().initMapObjects(routeMapVM.geoObjects!!, mapview, listener)
        mapview.map.addInputListener(inputListener)
        routeMapVM.viewState.value = ViewState.CONTENT
        userLocationObjectListener = CustomUserLocationObjectListener(context!!)
        userLocationLayer = mapKit.createUserLocationLayer(mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(userLocationObjectListener)
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onMasstransitRoutes(p0: MutableList<Route>) {
        if (p0.size > 0) {
            for (section in p0[0].getSections()) {
                YandexMapUtils().drawSection(
                    mapview,
                    SubpolylineHelper.subpolyline(
                        p0[0].getGeometry(), section.getGeometry()
                    )
                )
            }
        }
    }

    override fun onMasstransitRoutesError(p0: Error) {
        Log.e("error", p0.toString())
    }

    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            var info = mapObject.userData as GeoObject
            YandexMapUtils().selectObject(mapObject, mapview)
            routeMapVM.currentObject.postValue(info)
            routeMapVM.onMonumentClick(mapview, info)
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("permission", "granted")
                } else {
                }
                return
            }
            else -> {
            }
        }
    }

    @SuppressLint("NewApi")
    fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION
                )
            }
        } else {
            Log.e("permission", "granted")
            locationManager = MapKitFactory.getInstance().createLocationManager()
            showUserLocation()
        }
    }

    private fun showUserLocation() {
        locationManager!!.subscribeForLocationUpdates(
            0.0,
            600,
            1.0,
            false,
            FilteringMode.OFF,
            object : LocationListener {
                override fun onLocationStatusUpdated(p0: LocationStatus) {
                    Log.e("locationStatus", p0.name)
                }

                override fun onLocationUpdated(p0: Location) {
                    userLocationLayer.isVisible = true
                    mapview.map.move(
                        CameraPosition(p0.position, routeMapVM.currentZoom, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 1f),
                        null
                    )
                }
            })
    }
}