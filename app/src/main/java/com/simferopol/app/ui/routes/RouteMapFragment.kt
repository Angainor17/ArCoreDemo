package com.simferopol.app.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.simferopol.api.models.GeoObject
import com.simferopol.app.databinding.FragmentRouteMapBinding
import com.simferopol.app.ui.map.BaseMapFragment
import com.simferopol.app.ui.map.BaseMapVm
import com.simferopol.app.ui.map.simfer
import com.simferopol.app.ui.routes.vm.RouteMapVm
import com.simferopol.app.utils.models.ViewState
import com.simferopol.app.utils.ui.CustomInputListener
import com.simferopol.app.utils.ui.YandexMapUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_route_map.*
import kotlinx.android.synthetic.main.map_view.*

class RouteMapFragment : BaseMapFragment(), Session.RouteListener {

    lateinit var mapVM: RouteMapVm
    private val listener = YandexMapObjectTapListener()
    private lateinit var inputListener: CustomInputListener

    override fun createVm(): BaseMapVm = mapVM
    override fun getMapObjectTapListener(): MapObjectTapListener = YandexMapObjectTapListener()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: RouteMapFragmentArgs by navArgs()
        mapVM = RouteMapVm(this, args.route)
        val binding = FragmentRouteMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        footerContainer.visibility = View.VISIBLE
        inputListener = CustomInputListener(mapView)
        setRoutePoints()
    }

    override fun onStart() {
        super.onStart()
        if (!mapVM.geoObjects.isNullOrEmpty()) {
            YandexMapUtils().initMapObjects(mapVM.geoObjects!!, mapView, listener)
        }
        mapView.map.addInputListener(inputListener)
        mapVM.viewState.value = ViewState.CONTENT
    }

    override fun onMasstransitRoutes(mutableList: MutableList<Route>) {
        if (mutableList.size > 0) {
            for (section in mutableList[0].sections) {
                YandexMapUtils().drawSection(
                    mapView,
                    SubpolylineHelper.subpolyline(
                        mutableList[0].geometry, section.geometry
                    )
                )
            }
        }
    }

    override fun onMasstransitRoutesError(p0: Error) = Unit

    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            val info = mapObject.userData as GeoObject
            YandexMapUtils().selectObject(mapObject, mapView)
            mapVM.currentObject.postValue(info)
            mapVM.onMonumentClick(mapView, info)
            return true
        }
    }

    private fun setRoutePoints() {
        val points = ArrayList<RequestPoint>()
        TransportFactory.initialize(context)
        if ((mapVM.start.longitude != 0.0) and (mapVM.start.latitude !=  0.0))
            points.add(RequestPoint(mapVM.start, RequestPointType.WAYPOINT, null))
        mapVM.geoObjects?.forEach {
            if ((it.lon !=  0.0) and (it.lat !=  0.0)) {
                points.add(RequestPoint(Point(it.lat!!, it.lon!!), RequestPointType.WAYPOINT, null))
            }
        }
        if ((mapVM.finish.longitude !=  0.0) and (mapVM.finish.latitude !=  0.0))
            points.add(RequestPoint(mapVM.finish, RequestPointType.WAYPOINT, null))
        if (points.size > 1) {
            val mtRouter = TransportFactory.getInstance().createPedestrianRouter()
            mtRouter.requestRoutes(points, TimeOptions(), this)
            mapView.map.mapObjects.addPlacemark(
                points.first().point,
                ImageProvider.fromAsset(context, "map_route_point.png")
            )
            mapView.map.mapObjects.addPlacemark(
                points.last().point,
                ImageProvider.fromAsset(context, "map_route_point.png")
            )
            mapView.map.move(
                CameraPosition(points.first().point, mapVM.zoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
        } else mapView.map.move(
            CameraPosition(simfer, mapVM.zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
    }
}