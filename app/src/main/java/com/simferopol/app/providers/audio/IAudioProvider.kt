package com.simferopol.app.providers.audio

import android.view.View

interface IAudioProvider {

    fun initPlayerView(playerView: View)

    fun initAudioUrl(audioUrl: String)

    fun stopAudio()
}

enum class PlayerStatus {
    INIT, PAUSED, STOPPED, PLAYING
}