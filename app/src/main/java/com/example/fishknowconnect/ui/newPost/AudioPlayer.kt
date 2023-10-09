package com.example.fishknowconnect.ui.newPost

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fishknowconnect.R

@Composable
fun ShowAudioPlayer(audioFilePath: String) {
    val context = LocalContext.current
    val mMediaPlayer = MediaPlayer.create(context, Uri.parse(audioFilePath))
    Row {
        // IconButton for Start Action
        IconButton(onClick = { mMediaPlayer.start() }) {
            Icon(
                painter = painterResource(id = R.drawable.video),
                contentDescription = "",
                Modifier.size(100.dp)
            )
        }
        // IconButton for Pause Action
        IconButton(onClick = { mMediaPlayer.pause() }) {
            Icon(
                painter = painterResource(id = R.drawable.video),
                contentDescription = "",
                Modifier.size(100.dp)
            )
        }
    }

}