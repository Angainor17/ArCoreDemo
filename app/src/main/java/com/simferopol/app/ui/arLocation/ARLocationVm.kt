package com.simferopol.app.ui.arLocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.ui.arLocation.model.Venue
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class ARLocationVm : ViewModel() {

    private val apiManager by kodein.instance<ApiManager>()

    val venuesLiveData = MutableLiveData<ArrayList<Venue>>()

    init {
        GlobalScope.launch(IO) {
            val geoObjects = apiManager.getGeoObjects(null)
            if (geoObjects.success) {

                val venues = geoObjects.data?.map {
                    Venue(
                        it.name ?: "",
                        it.address ?: "",
                        it.lat ?: 0.0,
                        it.lon ?: 0.0,
                        ""
                    )
                }
                venues?.let { venuesLiveData.postValue(ArrayList(it)) }
            }
        }
    }
}