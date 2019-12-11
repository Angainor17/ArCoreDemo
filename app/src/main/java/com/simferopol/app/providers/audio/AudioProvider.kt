package com.simferopol.app.providers.audio

import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import java.io.File
import java.lang.ref.WeakReference


class AudioProvider : IAudioProvider {

    private val UPDATE_AUDIO_PROGRESS_BAR = 3
    lateinit var progressBar: ProgressBar
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var status = PlayerStatus.INIT
    private lateinit var audioProgressHandler: Handler
    private lateinit var audioProgressBarThread: Thread

    override fun progressBar(progressBarView: ProgressBar) {
        progressBar = progressBarView
        audioProgressHandler = AudioHandler(this)
    }

    override fun playClickListener(audioUrl: String) {
        when (status) {
            PlayerStatus.PAUSED -> {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                status = PlayerStatus.PLAYING
            }
            PlayerStatus.INIT -> {
                status = PlayerStatus.PLAYING
                val fileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1)
                val file =
                    File(progressBar.context.filesDir.toString() + "/downloads/" + fileName)
                if (!file.exists()) {
                    mediaPlayer.setDataSource(audioUrl)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                        progressBar.visibility = View.VISIBLE
                    }
                } else {
                    mediaPlayer.setDataSource(file.absolutePath)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                        progressBar.visibility = View.VISIBLE
                    }
                }

                audioProgressBarThread = object : Thread() {
                    override fun run() {
                        super.run()
                        try {
                            while (status != PlayerStatus.STOPPED) {
                                if (status != PlayerStatus.PAUSED) {
                                    if (audioProgressHandler != null) {
                                        val msg = Message()
                                        msg.what = UPDATE_AUDIO_PROGRESS_BAR
                                        audioProgressHandler.sendMessage(msg)
                                        sleep(1000)
                                    }
                                }
                            }
                        } catch (ex: InterruptedException) {
                            Log.e("audioThread", ex.toString())
                        }
                    }
                }
                audioProgressBarThread.start()
            }
            PlayerStatus.PLAYING -> {
                mediaPlayer.pause()
                status = PlayerStatus.PAUSED
            }
        }
    }

    override fun stopAudio() {
        status = PlayerStatus.INIT
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    inner class AudioHandler(providerInstance: AudioProvider) : Handler() {
        private val weakReference = WeakReference<AudioProvider>(providerInstance)

        override fun handleMessage(msg: Message) {
            val audioProvider = weakReference.get()
            if (audioProvider != null) {
                val audioPlayer = audioProvider.mediaPlayer
                super.handleMessage(msg)
                if ((msg.what === UPDATE_AUDIO_PROGRESS_BAR) and (status == PlayerStatus.PLAYING)) {

                    if (mediaPlayer != null) {
                        val currPlayPosition = audioPlayer.currentPosition
                        val totalTime = audioPlayer.duration
                        progressBar.max = totalTime
                        progressBar.progress = currPlayPosition
                    }
                }
            }
        }

    }

    enum class PlayerStatus {
        INIT, PAUSED, STOPPED, PLAYING
    }
}