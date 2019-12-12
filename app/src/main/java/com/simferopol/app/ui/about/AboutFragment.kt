package com.simferopol.app.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simferopol.app.databinding.FragmentAboutBinding
import com.simferopol.app.ui.audio.AudioFragment

class AboutFragment : AudioFragment() {

    private val aboutVm = AboutVm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = aboutVm
        val audioUrl = aboutVm.about.value?.audio
        audioUrl?.let { audioProvider.initAudioUrl(it) }

        return binding.root
    }
}