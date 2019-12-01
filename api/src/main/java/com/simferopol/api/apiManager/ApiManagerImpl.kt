package com.simferopol.api.apiManager

import android.content.Context
import com.google.gson.Gson
import com.simferopol.api.models.*
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

    override suspend fun getGeoObjects(categoryId: Int?): ManagerResult<ArrayList<GeoObject>> {
        val list = Gson().fromJson<ArrayList<GeoObject>>(
            readJson(context, "geoObjects.json")
        )
        return wrapManagerResult {
            if (categoryId != null)
                ArrayList(list.filter { geoObject -> geoObject.categoryId == categoryId })
            else list
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

    override suspend fun getStories(): ManagerResult<ArrayList<Story>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Story>>(
                readJson(
                    context,
                    "stories.json"
                )
            )
        }
    }

    override suspend fun getCategories(): ManagerResult<ArrayList<Category>> {
        return wrapManagerResult {
            Gson().fromJson<ArrayList<Category>>(
                readJson(
                    context,
                    "categories.json"
                )
            )
        }
    }
}