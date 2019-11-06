package com.simferopol.app.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.api.models.Route
import com.simferopol.api.routeManager.RouteManager
import com.simferopol.app.App
import com.simferopol.app.databinding.FragmentRouteBinding
import com.simferopol.app.ui.routes.vm.RouteVm
import org.kodein.di.generic.instance

class RouteFragment: Fragment() {

    private val routeManager by App.kodein.instance<RouteManager>()

    private val route = Route(
        7,
        "Деревянное кружево Енисейска",
        "https://yeniseysk-unesco.ru/uploads/route/preview/7/0_marshrut_preview.jpg",
        58.453376f,
        92.181447f,
        null,
        null,
        null,
        58.453775f,
        92.164252f,
        null,
        null,
        null,
        1.6f,
        0.3f,
        null,
        null,
        null
    )

    private val routeVm = RouteVm(route)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRouteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = routeVm

        return binding.root
    }

}