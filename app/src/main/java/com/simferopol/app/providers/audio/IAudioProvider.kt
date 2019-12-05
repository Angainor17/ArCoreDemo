package com.simferopol.app.providers.audio

import android.widget.ProgressBar

interface IAudioProvider {

    fun progressBar(progressBarView: ProgressBar)

    fun playClickListener(audioUrl: String)

    fun stopAudio()
}