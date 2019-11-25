package com.simferopol.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.simferopol.api.apiManager.ApiManager
import com.simferopol.api.models.GeoObject
import com.simferopol.api.utils.ManagerResult
import com.simferopol.app.App
import com.simferopol.app.YANDEX_MAP_API_KEY
import com.simferopol.app.utils.models.ViewState
import com.simferopol.app.utils.ui.CustomInputListener
import com.simferopol.app.utils.ui.CustomVisitor
import com.simferopol.app.utils.ui.YandexMapUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import java.lang.IllegalStateException

val simfer = Point(44.949684, 34.102521)

class MapFragment : Fragment() {

    private val mapVM = MapVM()
    lateinit var mapview: MapView
    lateinit var binding: FragmentMapBinding
    private val listener = YandexMapObjectTapListener()
    lateinit var inputListener: CustomInputListener
    lateinit var visitor: CustomVisitor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey(YANDEX_MAP_API_KEY)
        MapKitFactory.initialize(this.context)
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM
        visitor = CustomVisitor(context!!)
        inputListener = CustomInputListener(binding.mapview, binding.footerContainer)
        mapview = binding.mapview as MapView
        mapVM.mapview = mapview
        mapview.getMap().move(
            CameraPosition(simfer, mapVM.currentZoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        val args: MapFragmentArgs by navArgs()

        try {
            mapVM.currentObject.value = args.geoObject
            binding.footerContainer.visibility = View.VISIBLE
            binding.routeButton.visibility = View.GONE
            var point = simfer
            if (mapVM.currentObject.value!!.lat != null)
                point = Point(mapVM.currentObject.value?.lat!!, mapVM.currentObject.value?.lon!!)
            mapview.map.move(
                CameraPosition(point, mapVM.currentZoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
        } catch (exception: IllegalStateException) {
        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        val routeManager by App.kodein.instance<ApiManager>()
        super.onStart()
        mapview.map.mapObjects.clear()
        GlobalScope.launch(Dispatchers.Main) {
            val result: ManagerResult<List<GeoObject>> = if (mapVM.currentObject.value != null)
                routeManager.getGeoObjects(mapVM.currentObject.value?.categoryId)
            else routeManager.getGeoObjects(1)
            if (result.success) {
                mapVM.listOfGeoObjects.value = (ArrayList(result.data?.map { it } ?: ArrayList()))
                YandexMapUtils().initMapObjects(
                    mapVM.listOfGeoObjects.value!!,
                    mapview,
                    listener,
                    mapVM.currentObject.value
                )
            }
        }
        mapview.map.addInputListener(inputListener)
        mapVM.viewState.value = ViewState.CONTENT
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            var info = mapObject.userData as GeoObject
            var mark = mapObject as PlacemarkMapObject
            mapview.map.mapObjects.traverse(visitor)
            mark.setIcon(ImageProvider.fromAsset(context, info.activeIcon))
            mapVM.currentObject.postValue(info)
            binding.footerContainer.visibility = View.VISIBLE
            return true
        }
    }
}