package com.simferopol.app.ui.map

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.ui.map.base.IMapView
//import com.simferopol.app.utils.ui.YandexMapUtils
//import com.yandex.mapkit.map.MapObjectTapListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class MapVM(view: IMapView) : BaseMapVm(view) {

    private val apiManager by kodein.instance<ApiManager>()

    val currentObject = MutableLiveData<GeoObject>()
    val listOfGeoObjects = MutableLiveData(ArrayList<GeoObject>())

    fun onMonumentClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavMonument(currentObject.value!!)
        view.findNavController().navigate(action)
    }

    fun onSelectRouteClick(view: View) {
//        val action = MapFragmentDirections.actionNavMapToNavRoutes()
//        view.findNavController().navigate(action)
    }

    fun onArClick(view: View) {
        val action = MapFragmentDirections.actionNavMapToNavArLocation()
        view.findNavController().navigate(action)
    }

//    fun initData(mapObjectTapListener: MapObjectTapListener) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val result = if (currentObject.value != null)
//                apiManager.getGeoObjects(currentObject.value?.categoryId)
//            else apiManager.getGeoObjects(1)
//            if (result.success) {
//                GlobalScope.launch(Dispatchers.Main) {
//                    listOfGeoObjects.value = (ArrayList(result.data?.map { it } ?: ArrayList()))
//                    YandexMapUtils().initMapObjects(
//                        listOfGeoObjects.value!!,
//                        view.findMapView(),
//                        mapObjectTapListener,
//                        currentObject.value
//                    )
//                }
//            }
//        }
//    }

    fun initWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = apiManager.getWeather()
            if (result.success) {
                GlobalScope.launch(Dispatchers.Main) {
                    result.data?.let { view.setWeather(it) }
                }
            }
        }
    }
}