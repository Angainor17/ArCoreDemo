package com.simferopol.api.apiManager

import com.simferopol.api.models.AboutCityInfo
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.api.models.Story
import com.simferopol.api.utils.ManagerResult

interface ApiManager {

    suspend fun getRoutes(): ManagerResult<ArrayList<Route>>
    suspend fun getRoute(): ManagerResult<Route>
    suspend fun getGeoObjects(): ManagerResult<ArrayList<GeoObject>>
    suspend fun getAbout(): ManagerResult<AboutCityInfo>
    suspend fun getStories(): ManagerResult<ArrayList<Story>>


}