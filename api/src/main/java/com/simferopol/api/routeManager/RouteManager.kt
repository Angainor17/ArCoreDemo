package com.simferopol.api.routeManager

import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.api.utils.ManagerResult

interface RouteManager {

    suspend fun getRoutes(): ManagerResult<ArrayList<Route>>
    suspend fun getRoute(): ManagerResult<Route>
    suspend fun getGeoObjects(): ManagerResult<ArrayList<GeoObject>>

}