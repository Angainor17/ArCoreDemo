package com.simferopol.app.utils.ui

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App
import com.simferopol.app.R
import com.simferopol.app.providers.res.IResProvider
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_map.*
import org.kodein.di.direct
import org.kodein.di.generic.instance

class YandexMapUtils {

    fun selectObject(mapObject: MapObject, mapview: MapView) {
        val info = mapObject.userData as GeoObject
        val mark = mapObject as PlacemarkMapObject
        mapview.map.mapObjects.traverse(CustomVisitor(mapview.context))
        mark.setIcon(ImageProvider.fromAsset(mapview.context, info.activeIcon))
    }

    fun initMapObjects(
        list: ArrayList<GeoObject>,
        mapView: MapView,
        listener: MapObjectTapListener,
        currentObject: GeoObject? = null
    ) {
        lateinit var temp: MapObject

        list.forEach {
            if (it.lon != null) {
                temp = mapView.map.mapObjects.addPlacemark(
                    Point(it.lat!!, it.lon!!),
                    ImageProvider.fromAsset(mapView.context, it.icon)
                )
                if ((currentObject != null) and (it.id == currentObject?.id ?: false))
                    (temp as PlacemarkMapObject).setIcon(
                        ImageProvider.fromAsset(
                            mapView.context,
                            it.activeIcon
                        )
                    )
                temp.userData = it
                temp.addTapListener(listener)
            }
        }
    }

    fun drawSection(mapView: MapView, geometry: Polyline) {
        val resProvider: IResProvider = App.kodein.direct.instance()
        val polylineMapObject = mapView.map.mapObjects.addPolyline(geometry)
        polylineMapObject.strokeColor = resProvider.getColor(android.R.color.holo_blue_light)
        polylineMapObject.strokeWidth = 4f

    }
}

class CustomVisitor(
    private val context: Context,
    private val currentObject: GeoObject? = null
) :
    MapObjectVisitor {

    override fun onPolygonVisited(p0: PolygonMapObject) = Unit
    override fun onCircleVisited(p0: CircleMapObject) = Unit
    override fun onPolylineVisited(p0: PolylineMapObject) = Unit
    override fun onColoredPolylineVisited(p0: ColoredPolylineMapObject) = Unit
    override fun onCollectionVisitEnd(p0: MapObjectCollection) = Unit

    override fun onCollectionVisitStart(p0: MapObjectCollection): Boolean = true

    override fun onPlacemarkVisited(p0: PlacemarkMapObject) {
        lateinit var info: GeoObject
        if (p0.userData != null) {
            info = p0.userData as GeoObject
            p0.setIcon(ImageProvider.fromAsset(context, info.icon))
            if ((currentObject != null) and (info.id == currentObject?.id ?: false))
                p0.setIcon(ImageProvider.fromAsset(context, info.activeIcon))
        }
    }
}

class CustomInputListener(
    private val mapView: MapView,
    private val infoContainer: View? = null
) : InputListener {

    override fun onMapLongTap(p0: Map, p1: Point) {
        onMapTap(p0, p1)
    }

    override fun onMapTap(p0: Map, p1: Point) {
        mapView.map.mapObjects.traverse(CustomVisitor(mapView.context))
        if (infoContainer != null) {
            val animation = AnimationUtils.loadAnimation(mapView.context, R.anim.slide_out_bottom)
            infoContainer.startAnimation(animation)
            infoContainer.visibility = View.GONE
        }
    }
}

class CustomUserLocationObjectListener(val context: Context) : UserLocationObjectListener {

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectAdded(p0: UserLocationView) {
        val pinIcon = p0.pin
        pinIcon.setIcon(ImageProvider.fromAsset(context, "ic_route_point_current.png"))
        val pinArrow = p0.arrow
        pinArrow.setIcon(ImageProvider.fromAsset(context, "ic_route_point_current.png"))
        p0.accuracyCircle.isVisible = false
    }
}