package com.simferopol.app.ui.map.base

import android.app.Activity
import com.simferopol.api.models.Weather
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView

interface IMapView {

    fun showUserLocation()

    fun findActivity(): Activity

    fun findMapView(): MapView

    fun getMapObjectTapListener(): MapObjectTapListener

    fun setWeather(weather: Weather)

}