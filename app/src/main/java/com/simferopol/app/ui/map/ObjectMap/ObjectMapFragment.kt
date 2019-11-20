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


class ObjectMapFragment : Fragment() {

    private val mapVM = MapVM()
    lateinit var mapview: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey("30d70067-9f77-4a49-b74d-35fe453e79a1")
        MapKitFactory.initialize(this.context)
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = mapVM

        val args: ObjectMapFragmentArgs by navArgs()
        mapVM.currentObject.postValue(args.geoObject)

        // Укажите имя activity вместо map.
        mapview = binding.mapview as MapView
        mapview.getMap().move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
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
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
       // mapFragment.getMapAsync(mapVM)
    }
}