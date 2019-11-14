package com.simferopol.app.ui.routes.routeGeoObjects.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.simferopol.api.models.Route
import com.simferopol.api.dataManager.DataManager
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

    private val routeManager by kodein.instance<DataManager>()

    val list = MutableLiveData(ArrayList<GeoObjectVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { GeoObjectVm(it) } ?: ArrayList()))
            }
        }
    }

    fun onRouteClick(view: View) {
        val action = RouteFragmentDirections.actionNavRouteToNavRouteMap(route)
        view.findNavController().navigate(action)
        }
}