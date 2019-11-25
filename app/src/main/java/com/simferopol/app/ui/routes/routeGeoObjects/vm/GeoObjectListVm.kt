package com.simferopol.app.ui.routes.routeGeoObjects.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Route
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.ui.routes.routeGeoObjects.RouteFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class GeoObjectListVm(val route: Route) : ViewModel() {
    val name = route.name
    val imageUrl = route.preview
    val distance = route.distance
    val time = route.time
    var geoObjects = route.geoObjects

    private val routeManager by kodein.instance<ApiManager>()

    val list = MutableLiveData(ArrayList<GeoObjectVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects(1)
            if (result.success) {
                var tempGeoObject: GeoObject?
                var tempList = ArrayList<GeoObject>()
                geoObjects?.forEach {
                    tempGeoObject = result.data?.find { geoObject -> geoObject.id == it.id }
                    if (tempGeoObject != null) tempList.add(tempGeoObject!!)
                    tempGeoObject = null
                }
                list.postValue(ArrayList(tempList.map { GeoObjectVm(it) }))
                route.geoObjects = tempList
            }
        }
    }

    fun onRouteClick(view: View) {
        val action = RouteFragmentDirections.actionNavRouteToNavRouteMap(route)
        view.findNavController().navigate(action)
    }
}