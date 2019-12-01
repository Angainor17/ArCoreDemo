package com.simferopol.app.ui.map

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.ui.map.base.IMapView
import com.simferopol.app.utils.ui.YandexMapUtils
import com.yandex.mapkit.map.MapObjectTapListener
import kotlinx.coroutines.*
import org.kodein.di.generic.instance

class MapVM(view: IMapView) : BaseMapVm(view) {

    private val routeManager by kodein.instance<ApiManager>()

    val currentObject = MutableLiveData<GeoObject>()
    val listOfGeoObjects = MutableLiveData(ArrayList<GeoObject>())

    fun onMonumentClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavMonument(currentObject.value!!)
        view.findNavController().navigate(action)
    }

    fun onSelectRouteClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavRoutes()
        view.findNavController().navigate(action)
    }

    fun onArClick() {
        Log.e("ar", "click")// todo navigate to AR Screen
    }

    fun initData(mapObjectTapListener: MapObjectTapListener) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = if (currentObject.value != null)
                routeManager.getGeoObjects(currentObject.value?.categoryId)
            else routeManager.getGeoObjects(1)
            if (result.success) {
                GlobalScope.launch(Dispatchers.Main) {
                    listOfGeoObjects.value = (ArrayList(result.data?.map { it } ?: ArrayList()))
                    YandexMapUtils().initMapObjects(
                        listOfGeoObjects.value!!,
                        view.findMapView(),
                        mapObjectTapListener,
                        currentObject.value
                    )
                }
            }
        }
    }
}