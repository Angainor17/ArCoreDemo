package com.simferopol.app.ui.history.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.app.App.Companion.kodein
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class HistoryListVm : ViewModel() {

    private val apiManager by kodein.instance<ApiManager>()
    val list = MutableLiveData(ArrayList<HistoryVm>())
    var id = 0

    init {
        GlobalScope.launch {
            val result = apiManager.getStories()
            if (result.success) {
                list.postValue(ArrayList(result.data?.map { HistoryVm(it) } ?: ArrayList()))
            }
        }
    }
}