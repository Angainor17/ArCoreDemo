package com.simferopol.app.providers.audio

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import com.simferopol.app.utils.loadFile
import kotlinx.android.synthetic.main.audio_player_element.view.*
import java.io.File


class AudioProvider(private val context: Context) : IAudioProvider {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var status = PlayerStatus.INIT

    private var progressBar: ProgressBar? = null
    private var playButton: View? = null

    private var audioUrl: String = ""

    private val playingHandler = Thread {
        while (true) {
            try {
                Thread.sleep(1000)
                progressBar?.progress = mediaPlayer.currentPosition
                progressBar?.max = mediaPlayer.duration
            } catch (e: Exception) {

            }
        }
    }

    override fun initPlayerView(playerView: View) {
        val isPlayed = status == PlayerStatus.PLAYING

        playButton = playerView.playButton
        progressBar = playerView.progressBar

        playButton?.setOnClickListener {
            showAudioPlaying(playButton?.isActivated ?: false)
            playClickListener()
        }
        mediaPlayer.setOnCompletionListener {
            if (it.currentPosition >= it.duration) {
                resetAudio()
            }
        }

        playerView.visibility = if (isPlayed) GONE else VISIBLE
        playButton?.visibility = VISIBLE
        playButton?.isActivated = isPlayed
    }

    private fun resetAudio() {
        playingHandler.interrupt()
        mediaPlayer.reset()
        status = PlayerStatus.INIT
        showAudioPlaying(false)
    }

    private fun playClickListener() {
        when (status) {
            PlayerStatus.PAUSED -> {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                status = PlayerStatus.PLAYING
                showAudioPlaying(true)
            }
            PlayerStatus.INIT -> {
                showAudioPlaying(true)
                status = PlayerStatus.PLAYING
                val fileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1)
                val file =
                    File(context.filesDir.toString() + "/downloads/" + fileName)

                mediaPlayer.setDataSource(if (file.exists() && !file.isDirectory) file.absolutePath else audioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    progressBar?.visibility = VISIBLE
                }

                if (!playingHandler.isAlive) {
                    playingHandler.start()
                }
            }
            PlayerStatus.PLAYING -> {
                showAudioPlaying(false)
                mediaPlayer.pause()
                status = PlayerStatus.PAUSED
            }
            else -> {
                showAudioPlaying(false)
            }
        }
    }

    private fun showAudioPlaying(isPlaying: Boolean) {
        playButton?.isActivated = isPlaying
    }

    override fun initAudioUrl(audioUrl: String) {
        this.audioUrl = audioUrl
        loadFile(context, audioUrl)
    }

    override fun stopAudio() {
        status = PlayerStatus.INIT
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
}