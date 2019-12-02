package com.simferopol.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.marginBottom
import androidx.navigation.fragment.navArgs
import com.simferopol.api.models.GeoObject
import com.simferopol.app.R
import com.simferopol.app.databinding.FragmentMapBinding
import com.simferopol.app.utils.models.ViewState
import com.simferopol.app.utils.ui.CustomInputListener
import com.simferopol.app.utils.ui.CustomVisitor
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_view.*

val simfer = Point(44.949684, 34.102521)

class MapFragment : BaseMapFragment() {

    private val mapVM = MapVM(this)
    var mapObjectTapListener = YandexMapObjectTapListener()
    lateinit var inputListener: CustomInputListener
    override fun createVm(): BaseMapVm = mapVM

    override fun getMapObjectTapListener(): MapObjectTapListener = YandexMapObjectTapListener()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputListener = CustomInputListener(mapView, footerContainer)
        mapView.map.move(
            CameraPosition(simfer, mapVM.zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        arguments?.let {
            val args: MapFragmentArgs by navArgs()
            mapVM.currentObject.value = args.geoObject

            var point = simfer
            if (mapVM.currentObject.value!!.lat != null) {
                point = Point(mapVM.currentObject.value?.lat!!, mapVM.currentObject.value?.lon!!)
            }
            mapView.map.move(
                CameraPosition(point, mapVM.zoom, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
            footerContainer.visibility = View.VISIBLE
            routeButton.visibility = View.GONE
        }

        mapVM.initData(mapObjectTapListener)
    }

    override fun onStart() {
        super.onStart()
        mapView.map.mapObjects.clear()

        mapView.map.addInputListener(inputListener)
        mapVM.viewState.value = ViewState.CONTENT
    }

    inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            val info = mapObject.userData as GeoObject
            val mark = mapObject as PlacemarkMapObject
            mapView.map.mapObjects.traverse(CustomVisitor(context!!))
            mark.setIcon(ImageProvider.fromAsset(context, info.activeIcon))
            mapVM.currentObject.postValue(info)
            if (footerContainer.visibility != View.VISIBLE) {
                footerContainer.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
                footerContainer.startAnimation(animation)
            }
            return true
        }
    }
}