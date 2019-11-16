package com.simferopol.app.ui.services.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.Category
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class ServiceListVm(category: Category) : ViewModel() {

    private val category = category
    private val routeManager by kodein.instance<ApiManager>()

    val list = MutableLiveData(ArrayList<ServiceVm>())

    init {
        GlobalScope.launch {
            val result = routeManager.getGeoObjects(category.id)
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { ServiceVm(it) } ?: ArrayList()))
            }
        }
    }
}