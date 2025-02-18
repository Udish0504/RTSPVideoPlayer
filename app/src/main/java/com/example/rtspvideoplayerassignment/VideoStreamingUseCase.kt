package com.example.rtspvideoplayerassignment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer


class VideoStreamingUseCase(
    private val exoPlayer: ExoPlayer
) {

    fun getExoPlayer():ExoPlayer{
        return exoPlayer
    }

    fun startStreaming(rtspUrl: String) {
        val mediaItem = MediaItem.fromUri(rtspUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun stopStreaming() {
        exoPlayer.stop()
    }

    fun playStreaming(){
        exoPlayer.play()
    }

    fun pauseStreaming(){
        exoPlayer.pause()
    }
}
