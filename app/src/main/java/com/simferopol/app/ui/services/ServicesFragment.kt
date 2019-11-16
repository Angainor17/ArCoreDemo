package com.simferopol.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMonumentsBinding
import com.simferopol.app.databinding.FragmentServicesBinding
import com.simferopol.app.ui.monuments.vm.MonumentListVm
import com.simferopol.app.ui.services.vm.ServiceListVm

class ServicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: ServicesFragmentArgs by navArgs()
        val category = args.category
        val serviceListVm = ServiceListVm(category)
        val binding = FragmentServicesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = serviceListVm

        return binding.root
    }

}