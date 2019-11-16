package com.simferopol.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.databinding.FragmentServiceBinding
import com.simferopol.app.ui.services.vm.ServiceVm
import com.simferopol.app.ui.services.vm.ServicesListVm

class ServiceFragment : Fragment() {

    private val serviceVm = ServicesListVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentServiceBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = serviceVm

        return binding.root
    }
}