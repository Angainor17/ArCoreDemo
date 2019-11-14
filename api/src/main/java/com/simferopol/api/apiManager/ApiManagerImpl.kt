package com.simferopol.api.apiManager

import android.content.Context
import com.google.gson.Gson
import com.simferopol.api.models.AboutCityInfo
import com.simferopol.api.models.GeoObject
import com.simferopol.api.models.Route
import com.simferopol.api.utils.ManagerResult
import com.simferopol.api.utils.fromJson
import com.simferopol.api.utils.readJson
import com.simferopol.api.utils.wrapManagerResult

internal class ApiManagerImpl(private val context: Context) : ApiManager {

    override suspend fun getRoutes(): ManagerResult<ArrayList<Route>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Route>>(
                readJson(
                    context,
                    "routes.json"
                )
            )
        }
    }

    override suspend fun getRoute(): ManagerResult<Route> {
        return wrapManagerResult {
            Gson().fromJson<Route>(
                readJson(
                    context,
                    "route.json"
                )
            )
        }
    }

    override suspend fun getGeoObjects(): ManagerResult<ArrayList<GeoObject>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<GeoObject>>(
                readJson(
                    context,
                    "geoObjects.json"
                )
            )
        }
    }

    override suspend fun getAbout(): ManagerResult<AboutCityInfo> {
        return wrapManagerResult {
            Gson().fromJson<AboutCityInfo>(
                readJson(
                    context,
                    "about.json"
                )
            )
        }
    }
}