package com.simferopol.app.ui.audio

import androidx.fragment.app.Fragment
import com.simferopol.app.App.Companion.kodein
import com.simferopol.app.providers.audio.IAudioProvider
import org.kodein.di.generic.instance

open class AudioFragment : Fragment() {

    val audioProvider by kodein.instance<IAudioProvider>()

    override fun onDestroyView() {
        super.onDestroyView()
        audioProvider.stopAudio()
    }
}