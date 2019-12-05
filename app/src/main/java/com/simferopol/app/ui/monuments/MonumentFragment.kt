package com.simferopol.app.ui.monuments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.simferopol.app.App
import com.simferopol.app.databinding.FragmentMonumentBinding
import com.simferopol.app.providers.audio.IAudioProvider
import com.simferopol.app.ui.monuments.vm.MonumentVm
import com.simferopol.app.utils.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.audio_player_element.view.*
import org.kodein.di.generic.instance


class MonumentFragment : Fragment() {

    private val audioProvider by App.kodein.instance<IAudioProvider>()

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

        var audioUrl = monumentVM.audio
        if (!audioUrl.isNullOrEmpty()) {
            binding.player.visibility = View.VISIBLE
            audioProvider.progressBar(binding.player.progressBar)
            binding.player.play_button.setOnClickListener {
                binding.player.play_button.isActivated = !binding.player.play_button.isActivated
                audioProvider.playClickListener(audioUrl)
            }
        }

        if (!args.monument.slides.isNullOrEmpty()) {
            val adapter = object : ImagePagerAdapter() {}
            adapter.setItems(args.monument.slides!!)
            binding.photosViewpager.adapter = adapter
            binding.pagerTab.setupWithViewPager(binding.photosViewpager)
        } else binding.photosViewpager.visibility = View.GONE
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        audioProvider.stopAudio()
    }
}