package com.simferopol.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.App
import com.simferopol.app.databinding.FragmentServiceBinding
import com.simferopol.app.providers.audio.IAudioProvider
import com.simferopol.app.ui.services.vm.ServiceVm
import com.simferopol.app.utils.CustomFileUtils
import com.simferopol.app.utils.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.audio_player_element.view.*
import org.kodein.di.generic.instance


class ServiceFragment : Fragment() {

    private val audioProvider by App.kodein.instance<IAudioProvider>()

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

        val audioUrl = serviceVM.audio
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

        if (!args.service.slides.isNullOrEmpty()) {
            args.service.slides?.let {
                val adapter = object : ImagePagerAdapter() {}
                adapter.setItems(it)
                binding.photosViewpager.adapter = adapter
                binding.pagerTab.setupWithViewPager(binding.photosViewpager)
            }
        } else binding.photosViewpager.visibility = View.GONE
        return binding.root
    }

}