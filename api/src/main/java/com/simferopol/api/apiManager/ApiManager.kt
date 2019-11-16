package com.simferopol.api.apiManager

import com.simferopol.api.models.*
import com.simferopol.api.utils.ManagerResult

interface ApiManager {

    suspend fun getRoutes(): ManagerResult<ArrayList<Route>>
    suspend fun getRoute(): ManagerResult<Route>
    suspend fun getGeoObjects(categoryId: Int): ManagerResult<List<GeoObject>>
    suspend fun getAbout(): ManagerResult<AboutCityInfo>
    suspend fun getStories(): ManagerResult<ArrayList<Story>>
    suspend fun getCategories(): ManagerResult<ArrayList<Category>>
}