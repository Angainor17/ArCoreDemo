package com.simferopol.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import com.google.android.gms.maps.SupportMapFragment
//import com.simferopol.app.R
import com.simferopol.app.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import android.R
import androidx.navigation.fragment.navArgs
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

val apiKey = "30d70067-9f77-4a49-b74d-35fe453e79a1"
val simfer = Point(44.949684, 34.102521)
//val simferBounds = LatLngBounds(LatLng(44.888679, 34.010726), LatLng(45.009175, 34.191087))

class MapFragment : Fragment() {

    private val mapVM = MapVM()
    lateinit var mapview: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(this.context)
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM

        mapview = binding.mapview as MapView
        mapVM.mapview = mapview
        mapview.getMap().move(
            CameraPosition(simfer, mapVM.currentZoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

        binding.playButton.visibility = binding.footerContainer.visibility
        binding.modelButton.visibility = binding.footerContainer.visibility

        if (mapVM.currentObject.value != null)
        {
            binding.footerContainer.visibility = View.VISIBLE
            binding.playButton.visibility = binding.footerContainer.visibility
            binding.modelButton.visibility = binding.footerContainer.visibility
        }


        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
        mapVM.listOfGeoObjects.value?.forEach {
            mapview.map.mapObjects.addPlacemark(Point(it.lon!!, it.lat!!))
        }

    }
}