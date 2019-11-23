package com.simferopol.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import com.google.android.gms.maps.SupportMapFragment
//import com.simferopol.app.R
import com.simferopol.app.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import android.util.Log
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

val apiKey = "30d70067-9f77-4a49-b74d-35fe453e79a1"
val simfer = Point(44.949684, 34.102521)
//val simferBounds = LatLngBounds(LatLng(44.888679, 34.010726), LatLng(45.009175, 34.191087))

class MapFragment : Fragment() {

    private val mapVM = MapVM()
    lateinit var mapview: MapView
    lateinit var binding: FragmentMapBinding
    private val listener = YandexMapObjectTapListener()
    private val listener2 = MyInputListener()
    private val visitor = MyVisitor()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(this.context)
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM

        mapview = binding.mapview as MapView
        mapVM.mapview = mapview
        mapview.getMap().move(
            CameraPosition(simfer, mapVM.currentZoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        binding.playButton.visibility = binding.footerContainer.visibility
        binding.modelButton.visibility = binding.footerContainer.visibility

        if (mapVM.currentObject.value != null)
        {
            binding.footerContainer.visibility = View.VISIBLE
            binding.playButton.visibility = binding.footerContainer.visibility
            binding.modelButton.visibility = binding.footerContainer.visibility

        }
        return binding.root

    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }
    override fun onStart() {
        val routeManager by App.kodein.instance<ApiManager>()
        super.onStart()
        lateinit var temp: MapObject

        GlobalScope.launch(Dispatchers.Main) {
            val result = routeManager.getGeoObjects(null)
            if (result.success) {
                mapVM.listOfGeoObjects.postValue(ArrayList(result.data?.map { it } ?: ArrayList()))
                result.data?.forEach {
                    if (it.lon != null){
                       temp = mapview.map.mapObjects.addPlacemark(
                            Point(it.lat!!, it.lon!!),
                            ImageProvider.fromAsset(context,it.icon)
                        )
                    temp.userData = it

                    temp.addTapListener(listener)
//                    var mark = temp as PlacemarkMapObject
//                    var icon = mark.useCompositeIcon()
//                    icon.setIcon("pin", ImageProvider.fromAsset(context, it.icon),IconStyle())
//                    icon.setIcon("icon", ImageProvider.fromAsset(context, it.activeIcon),IconStyle())
                }
                }
                mapview.map.addInputListener(listener2)
                mapview.onStart()
                MapKitFactory.getInstance().onStart()
            }
        }

    }
    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            lateinit var temp: MapObject
            var info = mapObject.userData as GeoObject
            var mark = mapObject as PlacemarkMapObject
//            mapview.map.mapObjects.clear()
//            mapVM.listOfGeoObjects.value?.forEach {
//                if (it.lon != null){
//                    temp = mapview.map.mapObjects.addPlacemark(
//                        Point(it.lat!!, it.lon!!),
//                        ImageProvider.fromAsset(context,it.icon)
//                    )
//                    temp.userData = it
//                    temp.addTapListener(listener)
//
//                }
//            }
            mapview.map.mapObjects.traverse(visitor)
            mark.setIcon(ImageProvider.fromAsset(context,info.activeIcon))
            mapVM.currentObject.postValue(info)
            binding.footerContainer.visibility = View.VISIBLE
            return true
        }
    }


    private inner class MyInputListener : InputListener{
        override fun onMapLongTap(p0: Map, p1: Point) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onMapTap(p0: Map, p1: Point) {
            binding.footerContainer.visibility = View.GONE
            lateinit var temp: MapObject
            mapview.map.mapObjects.clear()
            mapVM.listOfGeoObjects.value?.forEach {
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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            var info = p0.userData as GeoObject
            p0.setIcon(ImageProvider.fromAsset(context,info.icon))
        }
    }
}