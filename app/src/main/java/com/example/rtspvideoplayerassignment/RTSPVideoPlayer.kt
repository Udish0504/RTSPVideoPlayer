package com.example.rtspvideoplayerassignment

import android.app.AlertDialog
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun RTSPVideoPlayer(
    videoStreamingUseCase: VideoStreamingUseCase
) {
    val context = LocalContext.current
    var isStreaming by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) } // To track buffering state
    var errorMessage by remember { mutableStateOf<String?>(null) } // To track error messages
    var rtspUrl by remember { mutableStateOf("") }
    var errorOccured by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = rtspUrl,
            onValueChange = { rtspUrl = it },
            label = { Text("Enter RTSP URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (rtspUrl!=""){
            isStreaming = true}
            try {
                videoStreamingUseCase.startStreaming(rtspUrl)
            }catch (e:Exception){
                Toast.makeText(context,"Error:${e.message}",Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Start Streaming")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isStreaming) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = videoStreamingUseCase.getExoPlayer()
                        useController = true
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier.alpha(if(isStreaming) 1f else 0f)){
            Button(onClick = { videoStreamingUseCase.playStreaming() }) {
                Text("Play")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { videoStreamingUseCase.pauseStreaming() }) {
                Text("Pause")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                videoStreamingUseCase.stopStreaming()
                isStreaming = false
            }) {
                Text("Stop")
            }
        }


    }

    videoStreamingUseCase.getExoPlayer().addListener(object : Player.Listener {
        @Deprecated("Deprecated in Java")
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_BUFFERING -> {
                    isBuffering = true
                }
                ExoPlayer.STATE_READY -> {
                    isBuffering = false
                }
                else -> {
                    isBuffering = false
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            errorMessage = "Error: ${error.message}"
            errorOccured = true
            Log.e("ExoPlayerError", error.message.orEmpty())
        }
    })

    if (errorOccured) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = { errorOccured = false },
            title = { Text("${errorMessage}") },
            backgroundColor = Color.Gray,
            buttons = { Button(modifier = Modifier.padding(20.dp).padding(start = 35.dp), onClick = {errorOccured = false}) { Text("Okay") } },
        )
    }

    if (isBuffering) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }


}
