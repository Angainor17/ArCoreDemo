package com.simferopol.app.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.simferopol.api.models.GeoObject
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
import kotlinx.android.synthetic.main.audio_player_element.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_view.*

val simfer = Point(44.949684, 34.102521)

class MapFragment : BaseMapFragment() {

    private val mapVM = MapVM(this)
    private val mapObjectTapListener = YandexMapObjectTapListener()
    private lateinit var inputListener: CustomInputListener

    override fun createVm(): BaseMapVm = mapVM
    lateinit var binding: FragmentMapBinding

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
        inputListener = CustomInputListener(mapView, footerContainer, player, audioProvider)
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
            setFooterContainer()
            routeButton.visibility = View.GONE
        }

        mapVM.initData(mapObjectTapListener)
        mapVM.initWeather()
    }

    override fun onStart() {
        super.onStart()

        mapView.map.addInputListener(inputListener)
        mapVM.viewState.value = ViewState.CONTENT
    }

    inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            val info = mapObject.userData as GeoObject
            val mark = mapObject as PlacemarkMapObject
            mapView.map.mapObjects.traverse(CustomVisitor(context!!))
            mark.setIcon(ImageProvider.fromAsset(context, info.activeIcon))
            mapVM.currentObject.value = info
            setFooterContainer()
            return true
        }
    }

    private fun setFooterContainer() {
        footerContainer.visibility = View.VISIBLE
        player.visibility = View.GONE
        find.visibility = View.GONE
        player.play_button.isActivated = false
        audioProvider.stopAudio()
        val audioUrl = mapVM.currentObject.value?.audio
        if (!audioUrl.isNullOrEmpty()) {
            player.visibility = View.VISIBLE
            audioProvider.progressBar(player.progressBar)
            player.play_button.setOnClickListener {
                player.play_button.isActivated = !player.play_button.isActivated
                audioProvider.playClickListener(audioUrl)
            }
        }
    }
}