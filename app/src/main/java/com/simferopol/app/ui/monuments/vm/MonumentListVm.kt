package com.simferopol.app.ui.monuments.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.models.GeoObject
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.ui.routes.routeGeoObjects.vm.GeoObjectVm
import com.simferopol.app.ui.routes.vm.RouteVm
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class MonumentListVm : ViewModel() {

    private val routeManager by kodein.instance<RouteManager>()

    val list = MutableLiveData(ArrayList<MonumentVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { MonumentVm(it) } ?: ArrayList()))
            }
        }
    }
}