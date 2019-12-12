package com.simferopol.app.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simferopol.app.databinding.FragmentAboutBinding
import com.simferopol.app.ui.audio.AudioFragment
import com.simferopol.app.utils.CustomFileUtils
import kotlinx.android.synthetic.main.audio_player_element.view.*

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
        var audioUrl = aboutVm.about.value?.audio
        binding.player.play_button.setOnClickListener {
            audioUrl = aboutVm.about.value?.audio
            if (!audioUrl.isNullOrEmpty()) {
                binding.player.visibility = View.VISIBLE
                audioProvider.progressBar(binding.player.progressBar)
                binding.player.play_button.isActivated = !binding.player.play_button.isActivated
                audioUrl?.let { audioProvider.playClickListener(it) }
            }
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
        return binding.root
    }
}