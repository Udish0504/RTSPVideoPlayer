package com.example.rtspvideoplayerassignment

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory


class ExoPlayerRepository {
    @Composable
    @OptIn(UnstableApi::class)
    fun createPlayer(): ExoPlayer {
        val context = LocalContext.current
        val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            5000,
            15000,
            2000,
            5000)
            .build()
        val exoPlayer = remember {
            ExoPlayer.Builder(context)
                    .setLoadControl(loadControl)
                    .setMediaSourceFactory(DefaultMediaSourceFactory(context)
                    .setLiveTargetOffsetMs(5000))
                    .build()
        }
        return exoPlayer
    }
}
