package com.simferopol.app.ui.routes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.SupportMapFragment
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App
import com.simferopol.app.databinding.FragmentMapBinding
import com.simferopol.app.databinding.FragmentRouteMapBinding
import com.simferopol.app.ui.map.MapVM
import com.simferopol.app.ui.map.simfer
import com.simferopol.app.ui.routes.vm.RouteMapVm
import com.simferopol.app.ui.routes.vm.RouteVm
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.runtime.Error
import com.yandex.mapkit.geometry.SubpolylineHelper
import android.R
import com.simferopol.app.providers.res.IResProvider
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.transport.Transport
import org.kodein.di.direct


class RouteMapFragment: Fragment(), Session.RouteListener {
    override fun onMasstransitRoutesError(p0: Error) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMasstransitRoutes(p0: MutableList<Route>) {
        if (p0.size > 0){
            for (section in p0.get(0).getSections()) {
                drawSection(
                    section.getMetadata().getData(),
                    SubpolylineHelper.subpolyline(
                        p0.get(0).getGeometry(), section.getGeometry()
                    )
                )

            }
        }
    }

    lateinit var routeMapVM: RouteMapVm
    private val listener = YandexMapObjectTapListener()
    private val listener2 = MyInputListener()
    private val visitor = MyVisitor()
    lateinit var mapview: MapView
    lateinit var binding: FragmentRouteMapBinding
    lateinit var mtRouter: PedestrianRouter

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

        val options = MasstransitOptions()

        val points = ArrayList<RequestPoint>()
        TransportFactory.initialize(context)
        routeMapVM.geoObjects?.forEach {
            if (it.lon != null) {
                points.add(RequestPoint(Point(it.lat!!, it.lon!!), RequestPointType.WAYPOINT, null))
            }
        }
        if(points.size > 1) {
            mtRouter = TransportFactory.getInstance().createPedestrianRouter()
            mtRouter.requestRoutes(points, TimeOptions(), this)
            mapview.getMap().move(
                CameraPosition(points.first().point, routeMapVM.currentZoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
        }
        else mapview.getMap().move(
            CameraPosition(simfer, routeMapVM.currentZoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }
    override fun onStart() {
        super.onStart()
        lateinit var temp: MapObject

        routeMapVM.geoObjects?.forEach {
            if (it.lon != null){
                temp = mapview.map.mapObjects.addPlacemark(
                    Point(it.lat!!, it.lon!!),
                    ImageProvider.fromAsset(context,it.icon)
                )
                temp.userData = it

                temp.addTapListener(listener)
            }
        }
                mapview.map.addInputListener(listener2)
                mapview.onStart()
                MapKitFactory.getInstance().onStart()
            }

    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            lateinit var temp: MapObject
            var info = mapObject.userData as GeoObject
            var mark = mapObject as PlacemarkMapObject
            mapview.map.mapObjects.traverse(visitor)
            mark.setIcon(ImageProvider.fromAsset(context,info.activeIcon))
            routeMapVM.currentObject.postValue(info)
            routeMapVM.onMonumentClick(mapview, info)
            return true
        }
    }


    private inner class MyInputListener : InputListener {
        override fun onMapLongTap(p0: Map, p1: Point) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onMapTap(p0: Map, p1: Point) {
            binding.footerContainer.visibility = View.GONE
            lateinit var temp: MapObject
            mapview.map.mapObjects.clear()
            routeMapVM.listOfGeoObjects.value?.forEach {
                if (it.lon != null){
                    temp = mapview.map.mapObjects.addPlacemark(
                        Point(it.lat!!, it.lon!!),
                        ImageProvider.fromAsset(context,it.icon)
                    )
                    temp.userData = it
                    temp.addTapListener(listener)

                }
            }
        }
    }

    private inner class MyVisitor: MapObjectVisitor {
        override fun onPolygonVisited(p0: PolygonMapObject) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCircleVisited(p0: CircleMapObject) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPolylineVisited(p0: PolylineMapObject) {
            Log.e("visit","end")
        }

        override fun onColoredPolylineVisited(p0: ColoredPolylineMapObject) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCollectionVisitEnd(p0: MapObjectCollection) {
            Log.e("visit","end")
        }

        override fun onCollectionVisitStart(p0: MapObjectCollection): Boolean {
            return true
        }

        override fun onPlacemarkVisited(p0: PlacemarkMapObject) {
            var info = p0.userData as  GeoObject
            p0.setIcon(ImageProvider.fromAsset(context,info.icon))
        }
    }



    private fun drawSection(data: SectionMetadata.SectionData, geometry: Polyline) {

        val resProvider: IResProvider = App.kodein.direct.instance()
        var polylineMapObject = mapview.map.mapObjects.addPolyline(geometry)
        polylineMapObject.strokeColor = resProvider.getColor(R.color.holo_blue_light)
        polylineMapObject.strokeWidth = 4f

    }
}