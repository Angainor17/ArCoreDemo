package com.simferopol.app.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.SupportMapFragment
import com.simferopol.app.R
import com.simferopol.app.databinding.FragmentMapBinding
import com.simferopol.app.databinding.FragmentRouteMapBinding
import com.simferopol.app.ui.map.MapVM
import com.simferopol.app.ui.routes.vm.RouteMapVm
import com.simferopol.app.ui.routes.vm.RouteVm


class RouteMapFragment: Fragment() {

    lateinit var routeMapVM: RouteMapVm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: RouteMapFragmentArgs by navArgs()
        routeMapVM = RouteMapVm(args.route)
        val binding = FragmentRouteMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = routeMapVM

        return binding.root
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(routeMapVM)
//    }
}