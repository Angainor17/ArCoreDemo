package com.simferopol.app.ui.monuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.simferopol.app.databinding.FragmentMonumentBinding
import com.simferopol.app.ui.audio.AudioFragment
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.utils.CustomFileUtils
import com.simferopol.app.utils.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.audio_player_element.view.*

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
            audioProvider.progressBar(binding.player.progressBar)
            binding.player.play_button.setOnClickListener {
                binding.player.play_button.isActivated = !binding.player.play_button.isActivated
                audioProvider.playClickListener(audioUrl)
            }
            binding.player.play_button.setOnLongClickListener {
                if (binding.player.download.visibility == View.VISIBLE) binding.player.download.visibility =
                    View.GONE
                else binding.player.download.visibility = View.VISIBLE
                true
            }
            binding.player.download.setOnClickListener {
                CustomFileUtils().loadFile(it.context, audioUrl)
                it.visibility = View.GONE
            }
        }

        if (!args.monument.slides.isNullOrEmpty()) {
            args.monument.slides?.let {
                val adapter = object : ImagePagerAdapter() {}
                adapter.setItems(it)
                binding.photosViewpager.adapter = adapter
                binding.pagerTab.setupWithViewPager(binding.photosViewpager)
            }
        } else binding.photosViewpager.visibility = View.GONE
        return binding.root
    }
}