package com.simferopol.app.ui.monuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMonumentBinding
import com.simferopol.app.databinding.FragmentMonumentsBinding
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.utils.ui.ImagePagerAdapter


class MonumentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: MonumentFragmentArgs by navArgs()
        val monumentVM = MonumentVm(args.monument)
        val binding = FragmentMonumentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = monumentVM

        if (!args.monument.slides.isNullOrEmpty()) {
            val adapter = object : ImagePagerAdapter() {}
            adapter.setItems(args.monument.slides!!)
            binding.photosViewpager.adapter = adapter
            binding.pagerTab.setupWithViewPager(binding.photosViewpager)
        }
        else  binding.photosViewpager.visibility = View.GONE
        return binding.root
    }

}