package com.simferopol.app.providers.audio

import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import java.lang.ref.WeakReference


class AudioProvider : IAudioProvider {

    private val UPDATE_AUDIO_PROGRESS_BAR = 3
    lateinit var progressBar: ProgressBar
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var status = "init"
    private lateinit var audioProgressHandler: Handler
    lateinit var audioProgressBarThread: Thread

    override fun progressBar(progressBarView: ProgressBar) {
        progressBar = progressBarView
        audioProgressHandler = AudioHandler(this)
    }

    override fun playClickListener(audioUrl: String) {
        when (status) {
            "paused" -> {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                status = "playing"
            }
            "init" -> {
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    it.start()
                    status = "playing"
                    progressBar.visibility = View.VISIBLE
                }
                audioProgressBarThread = object : Thread() {
                    override fun run() {
                        super.run()
                        try {
                            while (status != "paused") {
                                if (audioProgressHandler != null) {
                                    // Send update audio player progress message to main thread message queue.
                                    var msg = Message()
                                    msg.what = UPDATE_AUDIO_PROGRESS_BAR
                                    audioProgressHandler.sendMessage(msg)
                                    sleep(1000)
                                }
                            }
                        } catch (ex: InterruptedException) {
                            Log.e("audioThread", ex.toString())
                        }
                    }
                }
                audioProgressBarThread.start()
            }
            "playing" -> {
                mediaPlayer.pause()
                status = "paused"
            }
        }
    }

    override fun stopAudio() {
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
                if ((msg.what === UPDATE_AUDIO_PROGRESS_BAR) and (status == "playing")) {

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
}