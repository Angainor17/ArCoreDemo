package com.simferopol.app.ui.routes.routeGeoObjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentRouteBinding
import com.simferopol.app.ui.routes.routeGeoObjects.vm.GeoObjectListVm


class RouteFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: RouteFragmentArgs by navArgs()
        val geoObjectListVm =
            GeoObjectListVm(args.route)
        val binding = FragmentRouteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = geoObjectListVm

        return binding.root
    }

}