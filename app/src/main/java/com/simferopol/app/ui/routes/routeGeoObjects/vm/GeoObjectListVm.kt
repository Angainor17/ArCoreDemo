package com.simferopol.app.ui.routes.routeGeoObjects.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class GeoObjectListVm(
    val name: String,
    val imageUrl: String,
    val distance: Float,
    val time: Float) : ViewModel() {

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
}