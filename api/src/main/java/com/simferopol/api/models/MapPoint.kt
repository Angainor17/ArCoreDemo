package com.simferopol.api.models

class MapPoint : ArrayList<Float>() {

    fun lat() = get(0)

    fun lon() = get(1)
}