package com.simferopol.app.ui.arLocation.model

import com.simferopol.api.models.GeoObject

data class Venue(

    val geoObject: GeoObject,

    var name: String = geoObject.name ?: "",

    val address: String = geoObject.address ?: "",

    var lat: Double = geoObject.lat ?: 0.0,

    var long: Double = geoObject.lon ?: 0.0,

    val iconURL: String = ""

)