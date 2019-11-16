package com.simferopol.app.ui.services.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class CategoriesListVm : ViewModel() {

    private val apiManager by kodein.instance<ApiManager>()
    val list = MutableLiveData(ArrayList<CategoryVm>())

    init {
        GlobalScope.launch {
            val result = apiManager.getCategories()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { CategoryVm(it) } ?: ArrayList()))
            }
        }
    }
}