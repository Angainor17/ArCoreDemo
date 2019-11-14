package com.simferopol.app.ui.routes.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class RouteListVm : ViewModel() {

    private val routeManager by kodein.instance<ApiManager>()

    val list = MutableLiveData(ArrayList<RouteVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getRoutes()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { RouteVm(it) } ?: ArrayList()))
            }
        }
    }
}