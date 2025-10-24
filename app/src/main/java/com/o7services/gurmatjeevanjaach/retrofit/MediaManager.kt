package com.o7services.gurmatjeevanjaach.retrofit

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlin.math.sin

object MediaManager {
    var currentTitle: String? = null
    var currentUrl: String? = null

    var currentSingerName : String ?= null
    val currentTitleLiveData = MutableLiveData<String>()

    var currentAudioId : String ?= null
    var currentSingerId : String ?= null
    private var mediaPlayer: MediaPlayer? = null
    var isPlaying = false
    var onCompletion: (() -> Unit)? = null
    var onPrepared: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun playAudioFromUrl(title : String, audioUrl: String , audioId : String, singerId : String , singerName : String ) {
        releaseMediaPlayer()
        currentUrl = audioUrl
        currentTitle = title
        currentAudioId = audioId
        currentSingerName = singerName
        currentSingerId = singerId
        currentTitleLiveData.postValue(title)
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            try {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                    this@MediaManager.isPlaying = true
                    onPrepared?.invoke()
                }
                setOnCompletionListener {
                    currentTitle = ""
                    currentTitleLiveData.postValue("")
                    this@MediaManager.isPlaying = false
                    onCompletion?.invoke()
                }
                setOnErrorListener { _, what, extra ->
                    this@MediaManager.isPlaying = false
                    onError?.invoke("Error occurred: what=$what, extra=$extra")
                    true
                }
            } catch (e: Exception) {
                Log.e("MediaManager", "Playback error", e)
                onError?.invoke("Exception: ${e.message}")
            }
        }
    }


    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
        }
    }

    fun resume() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }
    fun hasCurrentTrack(): Boolean {
        // Returns true if a track is already loaded (so we can resume it)
        return !currentUrl.isNullOrEmpty()
    }

    fun play() {
        // Resume if paused
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }

    fun playNewTrack(title: String, audioUrl: String, audioId: String, singerId: String) {
        // Load and play a completely new track
     //   playAudioFromUrl(title, audioUrl, audioId, singerId , currentSingerName)
    }

    fun togglePlayPause() {
        if (isPlaying) pause() else resume()
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun isAudioPlaying(): Boolean {
        return isPlaying
    }
    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

}
