package com.simferopol.app.ui.monuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMonumentBinding
import com.simferopol.app.ui.audio.AudioFragment
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.utils.ui.ImagePagerAdapter

class MonumentFragment : AudioFragment() {

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

        val audioUrl = monumentVM.audio
        if (!audioUrl.isNullOrEmpty()) {
            binding.player.visibility = View.VISIBLE

            audioProvider.initAudioUrl(audioUrl)
            audioProvider.initPlayerView(binding.player)
        }

        if (!args.monument.slides.isNullOrEmpty()) {
            args.monument.slides?.let {
                binding.photosViewpager.adapter = object : ImagePagerAdapter() {}.apply {
                    setItems(it)
                }
                binding.pagerTab.setupWithViewPager(binding.photosViewpager)
            }
        } else {
            binding.photosViewpager.visibility = View.GONE
        }
        return binding.root
    }
}