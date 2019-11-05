package com.simferopol.app.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentRoutesBinding
import com.simferopol.app.ui.routes.vm.RouteListVm

class RoutesFragment : Fragment() {

    private val routesVm = RouteListVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRoutesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = routesVm

        return binding.root
    }
}