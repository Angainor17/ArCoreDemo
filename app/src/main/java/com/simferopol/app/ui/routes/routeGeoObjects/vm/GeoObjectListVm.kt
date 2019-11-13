package com.simferopol.app.ui.routes.routeGeoObjects.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.Route
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class GeoObjectListVm(val route: Route) : ViewModel() {
    val name = route.name
    val imageUrl = route.preview
    val distance = route.distance
    val time = route.time

    private val routeManager by kodein.instance<RouteManager>()

    val list = MutableLiveData(ArrayList<GeoObjectVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { GeoObjectVm(it) } ?: ArrayList()))
            }
        }
    }

    fun onRouteClick() {
            Log.e("routesBtn", name) //todo navigate to map 1 screen
        }
}