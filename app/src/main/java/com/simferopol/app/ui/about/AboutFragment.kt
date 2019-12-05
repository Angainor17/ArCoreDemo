package com.simferopol.app.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.simferopol.app.App
import com.simferopol.app.databinding.FragmentAboutBinding
import com.simferopol.app.providers.audio.IAudioProvider
import kotlinx.android.synthetic.main.audio_player_element.view.*
import org.kodein.di.generic.instance

class AboutFragment : Fragment() {

    private val audioProvider by App.kodein.instance<IAudioProvider>()

    private val aboutVm = AboutVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = aboutVm
        audioProvider.progressBar(binding.player.progressBar)
        binding.player.play_button.setOnClickListener {
            binding.player.play_button.isActivated = !binding.player.play_button.isActivated
            val audioUrl = aboutVm.about.value?.audio
            if (audioUrl != null)
                audioProvider.playClickListener(aboutVm.about.value!!.audio)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        audioProvider.stopAudio()
    }
}