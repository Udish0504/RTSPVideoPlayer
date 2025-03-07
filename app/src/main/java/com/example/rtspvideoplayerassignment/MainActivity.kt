package com.example.rtspvideoplayerassignment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rtspvideoplayerassignment.ui.theme.RTSPVideoPlayerAssignmentTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("AuthLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val exoPlayerRepository = ExoPlayerRepository()
            val videoStreamingUseCase = VideoStreamingUseCase(exoPlayerRepository.createPlayer())
            RTSPVideoPlayer(videoStreamingUseCase = videoStreamingUseCase)
        }
    }
}
