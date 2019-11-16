package com.simferopol.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMonumentBinding
import com.simferopol.app.databinding.FragmentMonumentsBinding
import com.simferopol.app.databinding.FragmentServiceBinding
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.ui.services.vm.ServiceVm
import com.simferopol.app.utils.ui.ImagePagerAdapter


class ServiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: ServiceFragmentArgs by navArgs()
        val serviceVM = ServiceVm(args.service)
        val binding = FragmentServiceBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = serviceVM

        if (!args.service.slides.isNullOrEmpty()) {
            val adapter = object : ImagePagerAdapter() {}
            adapter.setItems(args.service.slides!!)
            binding.photosViewpager.adapter = adapter
            binding.pagerTab.setupWithViewPager(binding.photosViewpager)
        } else binding.photosViewpager.visibility = View.GONE
        return binding.root
    }

}