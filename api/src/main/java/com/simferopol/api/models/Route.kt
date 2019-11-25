package com.simferopol.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Route(
    val id: Int,
    val name: String,
    val preview: String,
    @SerializedName("start_lat") val startLat: Float,
    @SerializedName("start_lon") val startLon: Float,
    @SerializedName("start_name") val startName: String?,
    @SerializedName("start_address") val startAddress: String?,
    @SerializedName("start_about") val startAbout: String?,
    @SerializedName("finish_lat") val finishLat: Float,
    @SerializedName("finish_lon") val finishLon: Float,
    @SerializedName("finish_name") val finishName: String?,
    @SerializedName("finish_address") val finishAddress: String?,
    @SerializedName("finish_about") val finishAbout: String?,
    val distance: Float,
    val time: Float,
    val audio: String?,
    @SerializedName("geoobjects") var geoObjects: ArrayList<GeoObject>?, //FIXME nullable for testing
    val points: ArrayList<MapPoint>? //FIXME nullable for testing
): Serializable