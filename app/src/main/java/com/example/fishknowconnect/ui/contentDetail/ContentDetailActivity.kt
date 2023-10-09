package com.example.fishknowconnect.ui.contentDetail

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.fishknowconnect.R
import com.example.fishknowconnect.ui.ToolBarLayout
import com.example.fishknowconnect.ui.contentDetail.ui.theme.FishKnowConnectTheme
import com.example.fishknowconnect.ui.newPost.ShowAudioPlayer
import com.example.fishknowconnect.ui.newPost.ShowVideoPlayer
import java.util.Locale

var tts: TextToSpeech? = null
var isListenEnable = false

class ContentDetailActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intentTitle = intent.getStringExtra("title")
        val intentContent = intent.getStringExtra("content")
        val intentFileUrl = intent.getStringExtra("file_url")
        val intentFileType = intent.getStringExtra("fileType")
        super.onCreate(savedInstanceState)
        setContent {
            FishKnowConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    // TextToSpeech(Context: this, OnInitListener: this)
                    tts = TextToSpeech(this@ContentDetailActivity, this)
                    Column {
                        ToolBarLayout(resources.getString(R.string.text_content_details))
                        ListItemDetailScreen(
                            intentTitle, intentContent, intentFileUrl, intentFileType
                        )
                    }
                }
            }
        }
    }

    /**
     * locale attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                isListenEnable = true
            }
        }
    }
}

@Composable
fun ListItemDetailScreen(
    title: String?,
    content: String?,
    fileUrl: String?,
    fileType: String?,
    modifier: Modifier = Modifier
) {
    //title
    Row(modifier = Modifier.padding(10.dp)) {
        Text(
            text = stringResource(id = R.string.text_title),
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .size(30.dp)
                .weight(1f)
        )
        Button(onClick = {
            if (isListenEnable) {
                tts!!.speak(title, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }) {
            Text(
                text = stringResource(id = R.string.text_listen),
                modifier = modifier,
                textAlign = TextAlign.End
            )
        }
    }
    if (!title.isNullOrEmpty()) {
        Text(text = title, modifier = Modifier.padding(10.dp))
    }
    //content
    Row(modifier = Modifier.padding(10.dp)) {
        Text(
            text = stringResource(id = R.string.text_content),
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .size(30.dp)
                .weight(1f)
        )
        Button(onClick = {
            if (isListenEnable) {
                tts!!.speak(content, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }) {
            Text(text = stringResource(id = R.string.text_listen), modifier = modifier)
        }
    }
    if (!content.isNullOrEmpty()) {
        Text(text = content, modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp))
    }
    if (!fileUrl.isNullOrEmpty()) {
        when (fileType) {
            "audio" -> ShowAudioPlayer(fileUrl)
            "image" -> Image(
                modifier = Modifier.padding(16.dp, 8.dp),
                painter = rememberAsyncImagePainter(fileUrl),
                contentDescription = null
            )
            "video" -> ShowVideoPlayer(videoUri = Uri.parse(fileUrl))
        }
    }
}
