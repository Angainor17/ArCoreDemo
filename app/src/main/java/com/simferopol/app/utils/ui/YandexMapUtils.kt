package com.simferopol.app.utils.ui

import android.R
import android.content.Context
import android.util.Log
import android.view.View
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App
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
import org.kodein.di.direct
import org.kodein.di.generic.instance

class YandexMapUtils {

    fun selectObject(mapObject: MapObject, mapview: MapView) {
        var info = mapObject.userData as GeoObject
        var mark = mapObject as PlacemarkMapObject
        mapview.map.mapObjects.traverse(CustomVisitor(mapview.context))
        mark.setIcon(ImageProvider.fromAsset(mapview.context, info.activeIcon))
    }

    fun initMapObjects(
        list: ArrayList<GeoObject>,
        mapview: MapView,
        listener: MapObjectTapListener,
        currentObject: GeoObject? = null
    ) {
        lateinit var temp: MapObject

        list.forEach {
            if (it.lon != null) {
                temp = mapview.map.mapObjects.addPlacemark(
                    Point(it.lat!!, it.lon!!),
                    ImageProvider.fromAsset(mapview.context, it.icon)
                )
                if ((currentObject != null) and (it.id == currentObject?.id ?: false))
                    (temp as PlacemarkMapObject).setIcon(ImageProvider.fromAsset(mapview.context, it.activeIcon))
                temp.userData = it
                temp.addTapListener(listener)
            }
        }
    }

    fun drawSection(mapview: MapView, geometry: Polyline) {
        val resProvider: IResProvider = App.kodein.direct.instance()
        var polylineMapObject = mapview.map.mapObjects.addPolyline(geometry)
        polylineMapObject.strokeColor = resProvider.getColor(R.color.holo_blue_light)
        polylineMapObject.strokeWidth = 4f

    }
}

class CustomVisitor(context: Context, currentObject: GeoObject? = null) : MapObjectVisitor {

    val context = context
    val currentObject = currentObject

    override fun onPolygonVisited(p0: PolygonMapObject) {
        TODO("not implemented")
    }

    override fun onCircleVisited(p0: CircleMapObject) {
        TODO("not implemented")
    }

    override fun onPolylineVisited(p0: PolylineMapObject) {
        Log.e("visit", "end")
    }

    override fun onColoredPolylineVisited(p0: ColoredPolylineMapObject) {
        TODO("not implemented")
    }

    override fun onCollectionVisitEnd(p0: MapObjectCollection) {
        Log.e("visit", "end")
    }

    override fun onCollectionVisitStart(p0: MapObjectCollection): Boolean {
        return true
    }

    override fun onPlacemarkVisited(p0: PlacemarkMapObject) {
        var info = p0.userData as GeoObject
        p0.setIcon(ImageProvider.fromAsset(context, info.icon))
        if ((currentObject != null) and (info.id == currentObject?.id ?: false))
            p0.setIcon(ImageProvider.fromAsset(context, info.activeIcon))
    }
}

class CustomInputListener(mapView: MapView, infoContainer: View?  = null ) : InputListener {

    val mapview = mapView
    val infoContainer = infoContainer

    override fun onMapLongTap(p0: Map, p1: Point) {
        onMapTap(p0, p1)
    }

    override fun onMapTap(p0: Map, p1: Point) {
        mapview.map.mapObjects.traverse(CustomVisitor(mapview.context))
        if (infoContainer != null) infoContainer.visibility = View.GONE

    }
}

class CustomUserLocationObjectListener(context: Context): UserLocationObjectListener {

    val context = context

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
//       Log.e("object", "updated")
//        var pinIcon = p0.pin
//        pinIcon.setIcon( ImageProvider.fromAsset(context, "ic_route_point_current.png"))
//        p0.accuracyCircle.isVisible = false

    }

    override fun onObjectRemoved(p0: UserLocationView) {
//        Log.e("object", "removed")
    }

    override fun onObjectAdded(p0: UserLocationView) {
       var pinIcon = p0.pin
        pinIcon.setIcon( ImageProvider.fromAsset(context, "ic_route_point_current.png"))
        p0.accuracyCircle.isVisible = false
        Log.e("object", "added")
    }

}