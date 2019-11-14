package com.simferopol.app.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentRouteMapBinding
import com.simferopol.app.ui.routes.vm.RouteVm


class RouteMapFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: RouteMapFragmentArgs by navArgs()
        val routeVM = RouteVm(args.route)
        val binding = FragmentRouteMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = routeVM

        return binding.root
    }

}