package com.simferopol.api.routeManager

import android.content.Context
import com.google.gson.Gson
import com.simferopol.api.models.Route
import com.simferopol.api.utils.ManagerResult
import com.simferopol.api.utils.fromJson
import com.simferopol.api.utils.readJson
import com.simferopol.api.utils.wrapManagerResult

internal class RouteManagerImpl(private val context: Context) : RouteManager {

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
}